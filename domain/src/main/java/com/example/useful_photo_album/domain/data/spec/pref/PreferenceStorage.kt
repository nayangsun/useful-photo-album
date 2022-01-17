package com.example.useful_photo_album.domain.data.spec.pref

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {
    suspend fun getAccessToken(sId: String)
    val accessToken: Flow<String>

    suspend fun completeOnboarding(complete: Boolean)
    val onboardingCompleted: Flow<Boolean>

    suspend fun showScheduleUiHints(show: Boolean)
    suspend fun areScheduleUiHintsShown(): Boolean

    suspend fun showNotificationsPreference(show: Boolean)
    val notificationsPreferenceShown: Flow<Boolean>

    suspend fun preferToReceiveNotifications(prefer: Boolean)
    val preferToReceiveNotifications: Flow<Boolean>

    suspend fun optInMyLocation(optIn: Boolean)
    val myLocationOptedIn: Flow<Boolean>

    suspend fun stopSnackbar(stop: Boolean)
    // TODO make this a flow or a suspend function
    suspend fun isSnackbarStopped(): Boolean

    suspend fun sendUsageStatistics(send: Boolean)
    val sendUsageStatistics: Flow<Boolean>

    suspend fun preferConferenceTimeZone(preferConferenceTimeZone: Boolean)
    val preferConferenceTimeZone: Flow<Boolean>

    suspend fun selectFilters(filters: String)
    val selectedFilters: Flow<String>

    suspend fun selectTheme(theme: String)
    val selectedTheme: Flow<String>

    suspend fun showCodelabsInfo(show: Boolean)
    val codelabsInfoShown: Flow<Boolean>
}