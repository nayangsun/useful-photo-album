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

package com.example.useful_photo_album.domain.userevent

import com.example.useful_photo_album.domain.users.StarUpdatedStatus
import com.example.useful_photo_album.shared.model.temp.Session
import com.example.useful_photo_album.shared.model.temp.SessionId
import com.example.useful_photo_album.shared.model.temp.userdata.UserEvent
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.domain.users.ReservationRequestAction
import com.example.useful_photo_album.domain.users.SwapRequestAction
import kotlinx.coroutines.flow.Flow

interface UserEventDataSource {

    fun getObservableUserEvents(userId: String): Flow<UserEventsResult>

    fun getObservableUserEvent(userId: String, eventId: SessionId): Flow<UserEventResult>

    fun getUserEvents(userId: String): List<UserEvent>

    fun getUserEvent(userId: String, eventId: SessionId): UserEvent?

    /**
     * Toggle the isStarred status for an event.
     *
     * @param userId the userId ([FirebaseUser#uid]) of the current logged in user
     * @param userEvent the [UserEvent], which isStarred is going to be the updated status
     * @return the Result that represents the status of the star operation.
     */
    suspend fun starEvent(userId: String, userEvent: UserEvent): Result<StarUpdatedStatus>

    suspend fun recordFeedbackSent(
        userId: String,
        userEvent: UserEvent
    ): Result<Unit>

    suspend fun requestReservation(
        userId: String,
        session: Session,
        action: ReservationRequestAction
    ): Result<ReservationRequestAction>

    suspend fun swapReservation(
        userId: String,
        fromSession: Session,
        toSession: Session
    ): Result<SwapRequestAction>

    fun clearSingleEventSubscriptions()
}

data class UserEventsResult(
    val userEvents: List<UserEvent>,
    val userEventsMessage: UserEventMessage? = null
)

data class UserEventResult(
    val userEvent: UserEvent?,
    val userEventMessage: UserEventMessage? = null
)