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

package com.example.useful_photo_album.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.useful_photo_album.presentation.databinding.FragmentFeedBinding
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import com.example.useful_photo_album.shared.model.temp.SessionId
import com.google.common.collect.ImmutableMap
import com.example.useful_photo_album.presentation.MainActivityViewModel
import com.example.useful_photo_album.presentation.MainNavigationFragment
import com.google.samples.apps.iosched.ui.feed.*
import com.example.useful_photo_album.presentation.feed.FeedNavigationAction.NavigateAction
import com.example.useful_photo_album.presentation.feed.FeedNavigationAction.NavigateToScheduleAction
import com.example.useful_photo_album.presentation.feed.FeedNavigationAction.NavigateToSession
import com.example.useful_photo_album.presentation.feed.FeedNavigationAction.OpenLiveStreamAction
import com.example.useful_photo_album.presentation.feed.FeedNavigationAction.OpenSignInDialogAction
import com.example.useful_photo_album.presentation.messages.SnackbarMessageManager
import com.example.useful_photo_album.presentation.messages.setupSnackbarManager
import com.example.useful_photo_album.presentation.signin.SignInDialogFragment
import com.example.useful_photo_album.presentation.util.launchAndRepeatWithViewLifecycle
import com.example.useful_photo_album.presentation.util.openWebsiteUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : MainNavigationFragment() {

    companion object {
        private const val DIALOG_NEED_TO_SIGN_IN = "dialog_need_to_sign_in"
        private const val BUNDLE_KEY_SESSIONS_LAYOUT_MANAGER_STATE = "sessions_layout_manager"
    }

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val model: FeedViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var binding: FragmentFeedBinding
    private var adapter: FeedAdapter? = null
    private lateinit var sessionsViewBinder: FeedSessionsViewBinder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = model
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::sessionsViewBinder.isInitialized) {
            outState.putParcelable(
                BUNDLE_KEY_SESSIONS_LAYOUT_MANAGER_STATE,
                sessionsViewBinder.recyclerViewManagerState
            )
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analyticsHelper.sendScreenView("Home", requireActivity())

        binding.toolbar.setupProfileMenuItem(mainActivityViewModel, viewLifecycleOwner)

        binding.root.doOnApplyWindowInsets { _, insets, _ ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.statusBar.run {
                layoutParams.height = systemBars.top
                isVisible = layoutParams.height > 0
                requestLayout()
            }
        }

        if (adapter == null) {
            // Initialising sessionsViewBinder here to handle config change.
            sessionsViewBinder =
                FeedSessionsViewBinder(
                    model,
                    savedInstanceState?.getParcelable(
                        BUNDLE_KEY_SESSIONS_LAYOUT_MANAGER_STATE
                    )
                )
        }

        binding.recyclerView.doOnApplyWindowInsets { v, insets, padding ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
        }

        binding.snackbar.doOnApplyWindowInsets { v, insets, padding ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
        }

        setupSnackbarManager(snackbarMessageManager, binding.snackbar)

        // Observe feed
        launchAndRepeatWithViewLifecycle {
            model.feed.collect {
                showFeedItems(binding.recyclerView, it)
            }
        }

        // Observe navigation events
        launchAndRepeatWithViewLifecycle {
            model.navigationActions.collect { action ->
                when (action) {
                    is NavigateAction -> findNavController().navigate(action.directions)
                    NavigateToScheduleAction ->
                        findNavController().navigate(FeedFragmentDirections.toSchedule())
                    is NavigateToSession -> openSessionDetail(action.sessionId)
                    is OpenLiveStreamAction -> openLiveStreamUrl(action.url)
                    OpenSignInDialogAction -> openSignInDialog()
                }
            }
        }
    }

    private fun openSessionDetail(id: SessionId) {
        // TODO support opening a session detail
        // findNavController().navigate(toSessionDetail(id))
    }

    private fun showFeedItems(recyclerView: RecyclerView, list: List<Any>?) {
        if (adapter == null) {
            val sectionHeaderViewBinder = FeedSectionHeaderViewBinder()
            val countdownViewBinder = CountdownViewBinder()
            val momentViewBinder = MomentViewBinder(
                eventListener = model,
                userInfo = model.userInfo,
                theme = model.theme
            )
            val sessionsViewBinder = FeedSessionsViewBinder(model)
            val feedAnnouncementsHeaderViewBinder =
                AnnouncementsHeaderViewBinder(this, model)
            val announcementViewBinder = AnnouncementViewBinder(model.timeZoneId, this)
            val announcementsEmptyViewBinder = AnnouncementsEmptyViewBinder()
            val announcementsLoadingViewBinder = AnnouncementsLoadingViewBinder()
            val feedSustainabilitySectionViewBinder = FeedSustainabilitySectionViewBinder()
            val feedSocialChannelsSectionViewBinder = FeedSocialChannelsSectionViewBinder()
            @Suppress("UNCHECKED_CAST")
            val viewBinders = ImmutableMap.builder<FeedItemClass, FeedItemBinder>()
                .put(
                    sectionHeaderViewBinder.modelClass,
                    sectionHeaderViewBinder as FeedItemBinder
                )
                .put(
                    countdownViewBinder.modelClass,
                    countdownViewBinder as FeedItemBinder
                )
                .put(
                    momentViewBinder.modelClass,
                    momentViewBinder as FeedItemBinder
                )
                .put(
                    sessionsViewBinder.modelClass,
                    sessionsViewBinder as FeedItemBinder
                )
                .put(
                    feedAnnouncementsHeaderViewBinder.modelClass,
                    feedAnnouncementsHeaderViewBinder as FeedItemBinder
                )
                .put(
                    announcementViewBinder.modelClass,
                    announcementViewBinder as FeedItemBinder
                )
                .put(
                    announcementsEmptyViewBinder.modelClass,
                    announcementsEmptyViewBinder as FeedItemBinder
                )
                .put(
                    announcementsLoadingViewBinder.modelClass,
                    announcementsLoadingViewBinder as FeedItemBinder
                )
                .put(
                    feedSustainabilitySectionViewBinder.modelClass,
                    feedSustainabilitySectionViewBinder as FeedItemBinder
                )
                .put(
                    feedSocialChannelsSectionViewBinder.modelClass,
                    feedSocialChannelsSectionViewBinder as FeedItemBinder
                )
                .build()

            adapter = FeedAdapter(viewBinders)
        }
        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }
        (recyclerView.adapter as FeedAdapter).submitList(list ?: emptyList())
        // After submitting the list to the adapter, the recycler view starts measuring and drawing
        // so let's wait for the layout to be drawn before reporting fully drawn.
        binding.recyclerView.doOnLayout {
            // reportFullyDrawn() prints `I/ActivityTaskManager: Fully drawn {activity} {time}`
            // to logcat. The framework ensures that the statement is printed only once for the
            // activity, so there is no need to add dedupping logic to the app.
            activity?.reportFullyDrawn()
        }
    }

    private fun openSignInDialog() {
        SignInDialogFragment().show(
            requireActivity().supportFragmentManager, DIALOG_NEED_TO_SIGN_IN
        )
    }

    private fun openLiveStreamUrl(url: String) {
        openWebsiteUrl(requireContext(), url)
    }
}
