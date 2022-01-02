package com.example.useful_photo_album.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.useful_photo_album.R
import com.example.useful_photo_album.databinding.ActivityMainBinding
import com.example.useful_photo_album.presentation.core.ui.NavigationHost
import dagger.hilt.android.AndroidEntryPoint

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

    }

    private fun configureNavMenu(menu: Menu) {
        menu.findItem(R.id.navigation_map)?.isVisible = mapFeatureEnabled
        menu.findItem(R.id.navigation_codelabs)?.isVisible = codelabsFeatureEnabled
        menu.findItem(R.id.navigation_explore_ar)?.apply {
            // Handle launching new activities, otherwise assume the destination is handled
            // by the nav graph. We want to launch a new Activity for only the AR menu item.
            isVisible = exploreArFeatureEnabled
            setOnMenuItemClickListener {
                if (connectivityManager.activeNetworkInfo?.isConnected == true) {
                    if (viewModel.arCoreAvailability.value?.isSupported == true) {
                        analyticsHelper.logUiEvent(
                            "Navigate to Explore I/O ARCore supported",
                            AnalyticsActions.CLICK
                        )
                        openExploreAr()
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


    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}