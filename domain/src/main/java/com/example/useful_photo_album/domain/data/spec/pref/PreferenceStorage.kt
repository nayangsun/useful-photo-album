package com.example.useful_photo_album.domain.data.spec.pref

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {
    suspend fun getAccessToken(sId: String)
    val accessToken: Flow<String>

    suspend fun stopSnackbar(stop: Boolean)
    // TODO make this a flow or a suspend function
    suspend fun isSnackbarStopped(): Boolean
}