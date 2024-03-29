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

package com.example.useful_photo_album.data

import com.example.useful_photo_album.shared.model.temp.ConferenceData
import com.example.useful_photo_album.data.db.AppDatabase
import com.example.useful_photo_album.data.db.CodelabFtsEntity
import com.example.useful_photo_album.data.db.SessionFtsEntity
import com.example.useful_photo_album.data.db.SpeakerFtsEntity
import com.example.useful_photo_album.shared.model.temp.ConferenceDay
import com.example.useful_photo_album.shared.util.TimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Single point of access to session data for the presentation layer.
 *
 * The session data is loaded from the bootstrap file.
 */
@Singleton
open class ConferenceDataRepository @Inject constructor(
    @Named("remoteConfDatasource") private val remoteDataSource: ConferenceDataSource,
    @Named("bootstrapConfDataSource") private val boostrapDataSource: ConferenceDataSource,
    private val appDatabase: AppDatabase
) {

    // In-memory cache of the conference data
    private var conferenceDataCache: ConferenceData? = null

    val currentConferenceDataVersion: Int
        get() = conferenceDataCache?.version ?: 0

    var latestException: Exception? = null
        private set

    var latestUpdateSource: UpdateSource = UpdateSource.NONE
        private set

    // Using a SharedFlow instead of StateFlow as there isn't an initial value to be emitted
    private val dataLastUpdatedFlow = MutableSharedFlow<Long>(replay = 1)
    val dataLastUpdatedObservable: Flow<Long> = dataLastUpdatedFlow

    // Prevents multiple consumers requesting data at the same time
    private val loadConfDataLock = Any()

    fun refreshCacheWithRemoteConferenceData() {
        val conferenceData = try {
            remoteDataSource.getRemoteConferenceData()
        } catch (e: IOException) {
            latestException = e
            throw e
        }
        if (conferenceData == null) {
            val e = Exception("Remote returned no conference data")
            latestException = e
            throw e
        }

        // Network data success!
        // Update cache
        synchronized(loadConfDataLock) {
            conferenceDataCache = conferenceData
            populateSearchData(conferenceData)
        }

        // Update meta
        latestException = null
        dataLastUpdatedFlow.tryEmit(System.currentTimeMillis())
        latestUpdateSource = UpdateSource.NETWORK
        latestException = null
    }

    fun getOfflineConferenceData(): ConferenceData {
        synchronized(loadConfDataLock) {
            val offlineData = conferenceDataCache ?: getCacheOrBootstrapDataAndPopulateSearch()
            conferenceDataCache = offlineData
            return offlineData
        }
    }

    private fun getCacheOrBootstrapDataAndPopulateSearch(): ConferenceData {
        val conferenceData = getCacheOrBootstrapData()
        populateSearchData(conferenceData)
        return conferenceData
    }

    private fun getCacheOrBootstrapData(): ConferenceData {
        // First, try the local cache:
        var conferenceData = remoteDataSource.getOfflineConferenceData()

        // Cache success!
        if (conferenceData != null) {
            latestUpdateSource = UpdateSource.CACHE
            return conferenceData
        }

        // Second, use the bootstrap file:
        conferenceData = boostrapDataSource.getOfflineConferenceData()!!
        latestUpdateSource = UpdateSource.BOOTSTRAP
        return conferenceData
    }

    open fun populateSearchData(conferenceData: ConferenceData) {
        val sessionFtsEntities = conferenceData.sessions.map { session ->
            SessionFtsEntity(
                sessionId = session.id,
                title = session.title,
                description = session.description,
                speakers = session.speakers.joinToString { it.name }
            )
        }
        appDatabase.sessionFtsDao().insertAll(sessionFtsEntities)
        val speakers = conferenceData.speakers.map {
            SpeakerFtsEntity(
                speakerId = it.id,
                name = it.name,
                description = it.biography
            )
        }
        appDatabase.speakerFtsDao().insertAll(speakers)
        val codelabs = conferenceData.codelabs.map {
            CodelabFtsEntity(
                codelabId = it.id,
                title = it.title,
                description = it.description
            )
        }
        appDatabase.codelabFtsDao().insertAll(codelabs)
    }

    open fun getConferenceDays(): List<ConferenceDay> = TimeUtils.ConferenceDays
}
