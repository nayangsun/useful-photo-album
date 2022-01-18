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

package com.example.useful_photo_album.data.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_ACCESS_TOKEN
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_CODELABS_INFO_SHOWN
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_CONFERENCE_TIME_ZONE
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_MY_LOCATION_OPTED_IN
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_NOTIFICATIONS_SHOWN
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_ONBOARDING
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_RECEIVE_NOTIFICATIONS
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_SCHED_UI_HINTS_SHOWN
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_SELECTED_FILTERS
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_SELECTED_THEME
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_SEND_USAGE_STATISTICS
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_SNACKBAR_IS_STOPPED
import com.google.samples.apps.iosched.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Storage for app and user preferences.
 */

@Singleton
class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {
    companion object {
        const val PREFS_NAME = "useful_photo_album"
        const val EMPTY_UNSPLASH_TOKEN = "have_no_token"
    }

    object PreferencesKeys {
        val PREF_ACCESS_TOKEN = stringPreferencesKey("pref_access_token")

        val PREF_ONBOARDING = booleanPreferencesKey("pref_onboarding")
        val PREF_SCHED_UI_HINTS_SHOWN = booleanPreferencesKey("pref_sched_ui_hints_shown")
        val PREF_NOTIFICATIONS_SHOWN = booleanPreferencesKey("pref_notifications_shown")
        val PREF_RECEIVE_NOTIFICATIONS = booleanPreferencesKey("pref_receive_notifications")
        val PREF_MY_LOCATION_OPTED_IN = booleanPreferencesKey("pref_my_location_opted_in")
        val PREF_SNACKBAR_IS_STOPPED = booleanPreferencesKey("pref_snackbar_is_stopped")
        val PREF_SEND_USAGE_STATISTICS = booleanPreferencesKey("pref_send_usage_statistics")
        val PREF_CONFERENCE_TIME_ZONE = booleanPreferencesKey("pref_conference_time_zone")
        val PREF_SELECTED_FILTERS = stringPreferencesKey("pref_selected_filters")
        val PREF_SELECTED_THEME = stringPreferencesKey("pref_dark_mode")
        val PREF_CODELABS_INFO_SHOWN = booleanPreferencesKey("pref_codelabs_info_shown")
    }

    override suspend fun getAccessToken(sId: String) {
        dataStore.edit {
            it[PREF_ACCESS_TOKEN] = sId
        }
    }

    override val accessToken = dataStore.data.map {
        it[PREF_ACCESS_TOKEN] ?: EMPTY_UNSPLASH_TOKEN
    }

    override suspend fun completeOnboarding(complete: Boolean) {
        dataStore.edit {
            it[PREF_ONBOARDING] = complete
        }
    }

    override val onboardingCompleted: Flow<Boolean> =
        dataStore.data.map { it[PREF_ONBOARDING] ?: false }

    override suspend fun showScheduleUiHints(show: Boolean) {
        dataStore.edit {
            it[PREF_SCHED_UI_HINTS_SHOWN] = show
        }
    }

    override suspend fun areScheduleUiHintsShown() =
        dataStore.data.map { it[PREF_SCHED_UI_HINTS_SHOWN] ?: false }.first()

    override suspend fun showNotificationsPreference(show: Boolean) {
        dataStore.edit {
            it[PREF_NOTIFICATIONS_SHOWN] = show
        }
    }

    override val notificationsPreferenceShown = dataStore.data.map {
        it[PREF_NOTIFICATIONS_SHOWN] ?: false
    }

    override suspend fun preferToReceiveNotifications(prefer: Boolean) {
        dataStore.edit {
            it[PREF_RECEIVE_NOTIFICATIONS] = prefer
        }
    }

    override val preferToReceiveNotifications = dataStore.data.map {
        it[PREF_RECEIVE_NOTIFICATIONS] ?: false
    }

    override suspend fun optInMyLocation(optIn: Boolean) {
        dataStore.edit {
            it[PREF_MY_LOCATION_OPTED_IN] = optIn
        }
    }

    override val myLocationOptedIn = dataStore.data.map {
        it[PREF_MY_LOCATION_OPTED_IN] ?: false
    }

    override suspend fun stopSnackbar(stop: Boolean) {
        dataStore.edit {
            it[PREF_SNACKBAR_IS_STOPPED] = stop
        }
    }

    override suspend fun isSnackbarStopped(): Boolean {
        return dataStore.data.map { it[PREF_SNACKBAR_IS_STOPPED] ?: false }.first()
    }

    override suspend fun sendUsageStatistics(send: Boolean) {
        dataStore.edit {
            it[PREF_SEND_USAGE_STATISTICS] = send
        }
    }

    override val sendUsageStatistics = dataStore.data.map {
        it[PREF_SEND_USAGE_STATISTICS] ?: true
    }

    override suspend fun preferConferenceTimeZone(preferConferenceTimeZone: Boolean) {
        dataStore.edit {
            it[PREF_CONFERENCE_TIME_ZONE] = preferConferenceTimeZone
        }
    }

    override val preferConferenceTimeZone =
        dataStore.data.map { it[PREF_CONFERENCE_TIME_ZONE] ?: true }

    override suspend fun selectFilters(filters: String) {
        dataStore.edit {
            it[PREF_SELECTED_FILTERS] = filters
        }
    }

    override val selectedFilters = dataStore.data.map {
        it[PREF_SELECTED_FILTERS] ?: ""
    }

    override suspend fun selectTheme(theme: String) {
        dataStore.edit {
            it[PREF_SELECTED_THEME] = theme
        }
    }

    override val selectedTheme =
        dataStore.data.map { it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey }

    override suspend fun showCodelabsInfo(show: Boolean) {
        dataStore.edit {
            it[PREF_CODELABS_INFO_SHOWN] = show
        }
    }

    override val codelabsInfoShown = dataStore.data.map {
        it[PREF_CODELABS_INFO_SHOWN] ?: false
    }
}
