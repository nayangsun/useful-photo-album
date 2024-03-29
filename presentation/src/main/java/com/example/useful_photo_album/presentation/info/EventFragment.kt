/*
 * Copyright 2018 Google LLC
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

package com.example.useful_photo_album.presentation.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.databinding.FragmentInfoEventBinding
import com.example.useful_photo_album.shared.di.AssistantAppEnabledFlag
import com.example.useful_photo_album.shared.model.temp.ConferenceWifiInfo
import com.example.useful_photo_album.shared.util.TimeUtils
import com.example.useful_photo_album.presentation.messages.SnackbarMessageManager
import com.example.useful_photo_album.presentation.messages.setupSnackbarManager
import com.example.useful_photo_album.presentation.util.doOnApplyWindowInsets
import com.example.useful_photo_album.presentation.util.launchAndRepeatWithViewLifecycle
import com.example.useful_photo_album.presentation.widget.FadingSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : Fragment() {

    @Inject lateinit var snackbarMessageManager: SnackbarMessageManager

    @Inject
    @JvmField
    @AssistantAppEnabledFlag
    var assistantAppEnabled = false

    private val eventInfoViewModel: EventInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context ?: return null

        val binding = FragmentInfoEventBinding.inflate(inflater, container, false).apply {
            viewModel = eventInfoViewModel
            showAssistantApp = assistantAppEnabled
            lifecycleOwner = viewLifecycleOwner
        }

        // Pad the bottom of the content so that it is above the nav bar
        binding.content.doOnApplyWindowInsets { v, insets, padding ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
        }

        val snackbarLayout = requireActivity().findViewById<FadingSnackbar>(R.id.snackbar)
        setupSnackbarManager(snackbarMessageManager, snackbarLayout)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchAndRepeatWithViewLifecycle {
            eventInfoViewModel.navigationActions.collect { event ->
                when (event) {
                    is EventInfoNavigationAction.OpenUrl -> {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.url)))
                    }
                }
            }
        }
    }
}

@BindingAdapter("countdownVisibility")
fun countdownVisibility(countdown: View, ignored: Boolean?) {
    // TODO Remove this method since ignored is unused
    countdown.visibility = if (TimeUtils.conferenceHasStarted()) GONE else VISIBLE
}

@BindingAdapter("wifiInfo")
fun bindWifiInfo(textView: TextView, wifiInfo: ConferenceWifiInfo?) {
    textView.text = if (wifiInfo == null) null else {
        textView.resources.getString(
            R.string.wifi_network_and_password, wifiInfo.ssid, wifiInfo.password
        )
    }
}
