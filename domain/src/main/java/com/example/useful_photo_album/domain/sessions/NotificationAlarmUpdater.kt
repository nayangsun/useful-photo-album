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

package com.example.useful_photo_album.domain.sessions

import com.example.useful_photo_album.di.ApplicationScope
import com.example.useful_photo_album.domain.component.notifications.SessionAlarmManager
import com.example.useful_photo_album.domain.userevent.ObservableUserEvents
import com.example.useful_photo_album.domain.userevent.SessionAndUserEventRepository
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.result.data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sets a notification for each session that is starred or reserved by the user.
 */
@Singleton
class NotificationAlarmUpdater @Inject constructor(
    private val alarmManager: SessionAlarmManager,
    private val repository: SessionAndUserEventRepository,
    @ApplicationScope private val externalScope: CoroutineScope
) {

    fun updateAll(userId: String) {
        externalScope.launch {
            val events = repository.getObservableUserEvents(userId).first { it is Result.Success }
            events.data?.let { data ->
                processEvents(userId, data)
            }
        }
    }

    private fun processEvents(
        userId: String,
        sessions: ObservableUserEvents
    ) {
        Timber.d("Setting all the alarms for user $userId")
        val startWork = System.currentTimeMillis()
        sessions.userSessions.forEach { session: UserSession ->
            if (session.userEvent.isStarred || session.userEvent.isReserved()) {
                alarmManager.setAlarmForSession(session)
            }
        }
        Timber.d("Work finished in ${System.currentTimeMillis() - startWork} ms")
    }

    fun cancelAll() {
        externalScope.launch {
            val events = repository.getObservableUserEvents(null).first { it is Result.Success }
            events.data?.let { data ->
                cancelAllSessions(data)
            }
        }
    }

    private fun cancelAllSessions(sessions: ObservableUserEvents) {
        Timber.d("Cancelling all the alarms")
        sessions.userSessions.forEach {
            alarmManager.cancelAlarmForSession(it.session.id)
        }
    }
}

@Singleton
open class StarReserveNotificationAlarmUpdater @Inject constructor(
    private val alarmManager: SessionAlarmManager
) {
    open fun updateSession(
        userSession: UserSession,
        requestNotification: Boolean
    ) {
        if (requestNotification) {
            alarmManager.setAlarmForSession(userSession)
        } else {
            alarmManager.cancelAlarmForSession(userSession.session.id)
        }
    }
}
