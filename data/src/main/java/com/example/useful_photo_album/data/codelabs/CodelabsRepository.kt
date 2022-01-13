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

package com.example.useful_photo_album.data.codelabs

import com.google.samples.apps.iosched.model.Codelab
import com.google.samples.apps.iosched.shared.data.ConferenceDataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CodelabsRepository @Inject constructor(
    private val conferenceDataRepository: ConferenceDataRepository
) {
    fun getCodelabs(): List<Codelab> = conferenceDataRepository.getOfflineConferenceData().codelabs
}
