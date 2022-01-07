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

package com.example.useful_photo_album.shared.model.temp.userdata

import com.example.useful_photo_album.shared.model.temp.Session
import com.example.useful_photo_album.shared.model.temp.SessionType
import com.example.useful_photo_album.shared.model.temp.userdata.UserEvent

/**
 * Wrapper class to hold the [Session] and associating [UserEvent].
 */
data class UserSession(
    val session: Session,
    val userEvent: UserEvent
) {

    fun isPostSessionNotificationRequired(): Boolean {
        return userEvent.isReserved() &&
            !userEvent.isReviewed &&
            session.type == SessionType.SESSION
    }
}
