/*
 * Copyright 2021 Google LLC
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

package com.example.useful_photo_album.presentation.sessiondetail

import com.example.useful_photo_album.domain.users.SwapRequestParameters
import com.example.useful_photo_album.presentation.reservation.RemoveReservationDialogParameters
import com.example.useful_photo_album.shared.model.temp.SessionId
import com.example.useful_photo_album.shared.model.temp.SpeakerId

sealed class SessionDetailNavigationAction {
    object NavigateToSignInDialogAction : SessionDetailNavigationAction()
    object ShowNotificationsPrefAction : SessionDetailNavigationAction()
    class NavigateToYoutube(val videoId: String) : SessionDetailNavigationAction()
    class RemoveReservationDialogAction(val params: RemoveReservationDialogParameters) :
        SessionDetailNavigationAction()
    class NavigateToSwapReservationDialogAction(val params: SwapRequestParameters) :
        SessionDetailNavigationAction()
    class NavigateToSessionFeedback(val sessionId: SessionId) : SessionDetailNavigationAction()
    class NavigateToSpeakerDetail(val speakerId: SpeakerId) : SessionDetailNavigationAction()
}
