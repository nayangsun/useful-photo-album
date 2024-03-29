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

package com.example.useful_photo_album.data.config

import com.example.useful_photo_album.shared.model.temp.ConferenceWifiInfo

interface AppConfigDataSource {

    fun getTimestamp(key: String): String // TODO: change name
    /**
     * Sync the strings with the latest values with Remote Config
     */
    suspend fun syncStrings()
    fun getWifiInfo(): ConferenceWifiInfo
    fun isMapFeatureEnabled(): Boolean
    fun isExploreArFeatureEnabled(): Boolean
    fun isCodelabsFeatureEnabled(): Boolean
    fun isSearchScheduleFeatureEnabled(): Boolean
    fun isSearchUsingRoomFeatureEnabled(): Boolean
    fun isAssistantAppFeatureEnabled(): Boolean
    fun isReservationFeatureEnabled(): Boolean
    fun isFeedEnabled(): Boolean
}
