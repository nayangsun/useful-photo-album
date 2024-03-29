/*
 * Copyright 2019 Google LLC
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

package com.example.useful_photo_album.presentation.codelabs.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.codelabs.ui.widget.CodelabsActionsHandler
import com.example.useful_photo_album.presentation.codelabs.ui.adapter.CodelabsAdapter
import com.example.useful_photo_album.presentation.databinding.FragmentCodelabsBinding
import com.example.useful_photo_album.shared.analytics.AnalyticsActions
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import com.example.useful_photo_album.shared.di.MapFeatureEnabledFlag
import com.example.useful_photo_album.shared.model.temp.Codelab
import com.example.useful_photo_album.shared.util.consume
import com.example.useful_photo_album.presentation.MainActivityViewModel
import com.example.useful_photo_album.presentation.MainNavigationFragment
import com.example.useful_photo_album.presentation.signin.setupProfileMenuItem
import com.example.useful_photo_album.presentation.util.doOnApplyWindowInsets
import com.example.useful_photo_album.presentation.util.launchAndRepeatWithViewLifecycle
import com.example.useful_photo_album.presentation.util.openWebsiteUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class CodelabsFragment : MainNavigationFragment(), CodelabsActionsHandler {

    companion object {
        private const val CODELABS_WEBSITE = "https://g.co/io/codelabs"
        private const val PARAM_UTM_SOURCE = "utm_source"
        private const val PARAM_UTM_MEDIUM = "utm_medium"
        private const val VALUE_UTM_SOURCE = "ioapp"
        private const val VALUE_UTM_MEDIUM = "android"
    }

    @Inject
    @field:Named("tagViewPool")
    lateinit var tagRecycledViewPool: RecycledViewPool

    @Inject
    @JvmField
    @MapFeatureEnabledFlag
    var mapFeatureEnabled: Boolean = false

    @Inject lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var binding: FragmentCodelabsBinding
    private val codelabsViewModel: CodelabsViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var codelabsAdapter: CodelabsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodelabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            setupProfileMenuItem(mainActivityViewModel, viewLifecycleOwner)
            menu.findItem(R.id.action_see_on_map)?.isVisible = mapFeatureEnabled
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_see_on_map -> consume { openCodelabsOnMap() }
                    R.id.action_codelabs_website -> consume { launchCodelabsWebsite() }
                }
                false
            }
        }

        codelabsAdapter = CodelabsAdapter(
            this,
            tagRecycledViewPool,
            savedInstanceState
        )
        binding.codelabsList.apply {
            adapter = codelabsAdapter
            setHasFixedSize(true)
        }

        // Pad the bottom of the RecyclerView so that the content scrolls up above the nav bar
        binding.codelabsList.doOnApplyWindowInsets { v, insets, padding ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
        }

        launchAndRepeatWithViewLifecycle {
            codelabsViewModel.screenContent.collect {
                codelabsAdapter.submitList(it)
            }
        }

        analyticsHelper.sendScreenView("Codelabs", requireActivity())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        codelabsAdapter.onSaveInstanceState(outState)
    }

    override fun dismissCodelabsInfoCard() {
        // Pass to ViewModel, which will update the list contents
        codelabsViewModel.dismissCodelabsInfoCard()
    }

    override fun openCodelabsOnMap() {
        findNavController().navigate(CodelabsFragmentDirections.toMap())
        // No analytics event, we log map marker selection in MapViewModel already.
    }

    override fun launchCodelabsWebsite() {
        openWebsiteUri(requireContext(), addCodelabsAnalyticsQueryParams(CODELABS_WEBSITE))
        analyticsHelper.logUiEvent("Codelabs Website", AnalyticsActions.CLICK)
    }

    override fun startCodelab(codelab: Codelab) {
        if (codelab.hasUrl()) {
            openWebsiteUri(requireContext(), addCodelabsAnalyticsQueryParams(codelab.codelabUrl))
            analyticsHelper.logUiEvent("Start codelab \"${codelab.title}\"", AnalyticsActions.CLICK)
        }
    }

    private fun addCodelabsAnalyticsQueryParams(url: String): Uri {
        return Uri.parse(url)
            .buildUpon()
            .appendQueryParameter(PARAM_UTM_SOURCE, VALUE_UTM_SOURCE)
            .appendQueryParameter(PARAM_UTM_MEDIUM, VALUE_UTM_MEDIUM)
            .build()
    }
}
