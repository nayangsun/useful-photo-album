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

package com.example.useful_photo_album.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.prefs.NotificationsPrefSaveActionUseCase
import com.example.useful_photo_album.domain.settings.*
import com.example.useful_photo_album.shared.result.data
import com.example.useful_photo_album.shared.util.tryOffer
import com.google.samples.apps.iosched.model.Theme
import com.example.useful_photo_album.presentation.util.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val setTimeZoneUseCase: SetTimeZoneUseCase,
    getTimeZoneUseCase: GetTimeZoneUseCase,
    val notificationsPrefSaveActionUseCase: NotificationsPrefSaveActionUseCase,
    getNotificationsSettingUseCase: GetNotificationsSettingUseCase,
    val setAnalyticsSettingUseCase: SetAnalyticsSettingUseCase,
    getAnalyticsSettingUseCase: GetAnalyticsSettingUseCase,
    val setThemeUseCase: SetThemeUseCase,
    getThemeUseCase: GetThemeUseCase,
    getAvailableThemesUseCase: GetAvailableThemesUseCase
) : ViewModel() {

    // Used to re-run flows on command
    private val refreshSignal = MutableSharedFlow<Unit>()
    // Used to run flows on init and also on command
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    // Time Zone setting
    val preferConferenceTimeZone: StateFlow<Boolean> = loadDataSignal.mapLatest {
        Timber.e("들어옴")
        getTimeZoneUseCase(Unit).data ?: true
    }.stateIn(viewModelScope, WhileViewSubscribed, true)

    val enableNotifications: StateFlow<Boolean> = loadDataSignal.mapLatest {
        Timber.e("들어옴")
        getNotificationsSettingUseCase(Unit).data ?: true
    }.stateIn(viewModelScope, WhileViewSubscribed, true)

    // Analytics setting
    val sendUsageStatistics =
        loadDataSignal.combine(getAnalyticsSettingUseCase(Unit)) { _, result ->
            Timber.e("들어옴")
            result.data ?: false
        }.stateIn(viewModelScope, WhileViewSubscribed, false)

    // Theme setting
    val theme: StateFlow<Theme> = loadDataSignal.mapLatest {
        Timber.e("들어옴")
        getThemeUseCase(Unit).data ?: Theme.SYSTEM
    }.stateIn(viewModelScope, WhileViewSubscribed, Theme.SYSTEM)

    // Theme setting
    val availableThemes: StateFlow<List<Theme>> = loadDataSignal.mapLatest {
        Timber.e("들어옴")
        getAvailableThemesUseCase(Unit).data ?: listOf<Theme>()
    }.stateIn(viewModelScope, WhileViewSubscribed, listOf())

    // SIDE EFFECTS: Navigation actions
    private val _navigationActions = Channel<SettingsNavigationAction>(capacity = Channel.CONFLATED)
    // Exposed with receiveAsFlow to make sure that only one observer receives updates.
    val navigationActions = _navigationActions.receiveAsFlow()

    private suspend fun refreshData() {
        refreshSignal.emit(Unit)
    }

    fun toggleTimeZone() {
        viewModelScope.launch {
            setTimeZoneUseCase(!preferConferenceTimeZone.value)
            refreshData()
        }
    }

    fun toggleEnableNotifications() {
        viewModelScope.launch {
            notificationsPrefSaveActionUseCase(!enableNotifications.value)
            refreshData()
        }
    }

    fun toggleSendUsageStatistics() {
        viewModelScope.launch {
            setAnalyticsSettingUseCase(!sendUsageStatistics.value)
            refreshData()
        }
    }

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }

    fun onThemeSettingClicked() {
        _navigationActions.tryOffer(SettingsNavigationAction.NavigateToThemeSelector)
    }
}

sealed class SettingsNavigationAction {
    object NavigateToThemeSelector : SettingsNavigationAction()
}
