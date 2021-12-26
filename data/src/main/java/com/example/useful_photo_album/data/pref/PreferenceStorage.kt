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

package com.example.useful_photo_album.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.useful_photo_album.pref.DataStorePreferenceStorage.PreferencesKeys.PREF_ACCESS_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Storage for app and user preferences.
 */
interface PreferenceStorage {
    suspend fun getAccessToken(sId: String)
    val accessToken: Flow<String>
}

@Singleton
class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {
    companion object {
        const val PREFS_NAME = "useful_photo_album"
    }

    object PreferencesKeys {
        val PREF_ACCESS_TOKEN = stringPreferencesKey("pref_access_token")
    }

    override suspend fun getAccessToken(sId: String) {
        dataStore.edit {
            it[PREF_ACCESS_TOKEN] = sId
        }
    }

    override val accessToken = dataStore.data.map {
        it[PREF_ACCESS_TOKEN] ?: "no_have_token"
    }
}
