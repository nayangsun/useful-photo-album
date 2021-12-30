package com.example.useful_photo_album.domain.data.spec.datastore

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {
    suspend fun getAccessToken(sId: String)
    val accessToken: Flow<String>
}