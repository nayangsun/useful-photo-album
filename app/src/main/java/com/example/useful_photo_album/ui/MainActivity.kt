/*
 * Copyright 2022 Malgeon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.useful_photo_album.ui

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.useful_photo_album.R
import com.example.useful_photo_album.databinding.ActivityMainBinding
import com.example.useful_photo_album.presentation.core.ui.MainNavigationFragment
import com.example.useful_photo_album.presentation.core.ui.NavigationHost
import com.example.useful_photo_album.shared.analytics.AnalyticsActions
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationHost {

    companion object {
        /** Key for an int extra defining the initial navigation target. */
        const val EXTRA_NAVIGATION_ID = "extra.NAVIGATION_ID"

        private const val NAV_ID_NONE = -1

        private const val DIALOG_SIGN_IN = "dialog_sign_in"
        private const val DIALOG_SIGN_OUT = "dialog_sign_out"

        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_feed,
            R.id.navigation_schedule,
            R.id.navigation_map,
            R.id.navigation_info,
            R.id.navigation_agenda,
            R.id.navigation_codelabs,
            R.id.navigation_settings
        )
    }

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private val viewModel: MainActivityViewModel by viewModels()

    private var currentNavId = NAV_ID_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentNavId = destination.id
            // TODO: hide nav if not a top-level destination?
        }

        // Either of two different navigation views might exist depending on the configuration.
        binding.bottomNavigation?.apply {
            configureNavMenu(menu)
            setupWithNavController(navController)
            setOnItemReselectedListener { } // prevent navigating to the same item
        }

        binding.navigationRail?.apply {
            configureNavMenu(menu)
            setupWithNavController(navController)
            setOnItemReselectedListener { } // prevent navigating to the same item
        }

        if (savedInstanceState == null) {
            currentNavId = navController.graph.startDestination
            val requestedNavId = intent.getIntExtra(EXTRA_NAVIGATION_ID, currentNavId)
            navigateTo(requestedNavId)
        }

        binding.navigationRail?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { view, insets ->
                // Pad the Navigation Rail so its content is not behind system bars.
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updatePadding(top = systemBars.top, bottom = systemBars.bottom)
                insets
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootContainer) { view, insets ->
            // Hide the bottom navigation view whenever the keyboard is visible.
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            binding.bottomNavigation?.isVisible = !imeVisible

            // If we're showing the bottom navigation, add bottom padding. Also, add left and right
            // padding since there's no better we can do with horizontal insets.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val bottomPadding = if (binding.bottomNavigation?.isVisible == true) {
                systemBars.bottom
            } else 0
            view.updatePadding(
                left = systemBars.left,
                right = systemBars.right,
                bottom = bottomPadding
            )
            // Consume the insets we've used.
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(0, systemBars.top, 0, systemBars.bottom - bottomPadding)
            ).build()
        }
    }

    private fun configureNavMenu(menu: Menu) {
        menu.findItem(R.id.navigation_map)?.isVisible = false
        menu.findItem(R.id.navigation_codelabs)?.isVisible = false
        menu.findItem(R.id.navigation_explore_ar)?.apply {
            // Handle launching new activities, otherwise assume the destination is handled
            // by the nav graph. We want to launch a new Activity for only the AR menu item.
            isVisible = false
            setOnMenuItemClickListener {
                if (connectivityManager.activeNetworkInfo?.isConnected == true) {
                    if (false) {
                        analyticsHelper.logUiEvent(
                            "Navigate to Explore I/O ARCore supported",
                            AnalyticsActions.CLICK
                        )
//                        openExploreAr()
                    } else {
                        analyticsHelper.logUiEvent(
                            "Navigate to Explore I/O ARCore NOT supported",
                            AnalyticsActions.CLICK
                        )
                        openArCoreNotSupported()
                    }
                } else {
                    openNoConnection()
                }
                true
            }
        }
    }

    private fun navigateTo(navId: Int) {
        if (navId == currentNavId) {
            return // user tapped the current item
        }
        navController.navigate(navId)
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun getCurrentFragment(): MainNavigationFragment? {
        return navHostFragment
            .childFragmentManager
            .primaryNavigationFragment as? MainNavigationFragment
    }

//    private fun openExploreAr() {
//        val intent = Intent(
//            this,
//            ArActivity::class.java
//        ).apply {
//            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//            putExtra(ArConstants.CAN_SIGNED_IN_USER_DEMO_AR, viewModel.canSignedInUserDemoAr.value)
//            putExtra(ArConstants.PINNED_SESSIONS_JSON_KEY, viewModel.pinnedSessionsJson.value)
//        }
//        startActivity(intent)
//    }


    private fun openNoConnection() {
        navigateTo(R.id.navigation_no_network_ar)
    }

    private fun openArCoreNotSupported() {
        navigateTo(R.id.navigation_phone_does_not_support_arcore)
    }
}
