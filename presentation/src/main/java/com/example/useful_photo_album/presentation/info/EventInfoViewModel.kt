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

import android.net.wifi.WifiConfiguration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.logistics.LoadWifiInfoUseCase
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.util.wifi.WifiInstaller
import com.example.useful_photo_album.shared.analytics.AnalyticsActions
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import com.example.useful_photo_album.shared.model.temp.ConferenceWifiInfo
import com.example.useful_photo_album.shared.result.data
import com.example.useful_photo_album.shared.util.tryOffer
import com.example.useful_photo_album.presentation.messages.SnackbarMessage
import com.example.useful_photo_album.presentation.messages.SnackbarMessageManager
import com.example.useful_photo_album.presentation.util.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EventInfoViewModel @Inject constructor(
    loadWifiInfoUseCase: LoadWifiInfoUseCase,
    private val wifiInstaller: WifiInstaller,
    private val analyticsHelper: AnalyticsHelper,
    private val snackbarMessageManager: SnackbarMessageManager
) : ViewModel() {

    companion object {
        private const val ASSISTANT_APP_URL =
            "https://assistant.google.com/services/invoke/uid/0000009fca77b068"
    }

    val wifiConfig: StateFlow<ConferenceWifiInfo?> = flow {
        emit(loadWifiInfoUseCase(Unit).data)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val showWifi: StateFlow<Boolean> = wifiConfig.map {
        it?.ssid?.isNotBlank() == true && it.password.isNotBlank()
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    private val _navigationActions = Channel<EventInfoNavigationAction>(Channel.CONFLATED)
    val navigationActions = _navigationActions.receiveAsFlow()

    fun onWifiConnect() {
        val config = wifiConfig.value ?: return
        val success = wifiInstaller.installConferenceWifi(
            WifiConfiguration().apply {
                SSID = config.ssid
                preSharedKey = config.password
            }
        )
        val snackbarMessage = if (success) {
            SnackbarMessage(R.string.wifi_install_success)
        } else {
            SnackbarMessage(
                messageId = R.string.wifi_install_clipboard_message, longDuration = true
            )
        }

        snackbarMessageManager.addMessage(snackbarMessage)
        analyticsHelper.logUiEvent("Events", AnalyticsActions.WIFI_CONNECT)
    }

    fun onClickAssistantApp() {
        _navigationActions.tryOffer(EventInfoNavigationAction.OpenUrl(ASSISTANT_APP_URL))
        analyticsHelper.logUiEvent("Assistant App", AnalyticsActions.CLICK)
    }
}

sealed class EventInfoNavigationAction {
    data class OpenUrl(val url: String) : EventInfoNavigationAction()
}
