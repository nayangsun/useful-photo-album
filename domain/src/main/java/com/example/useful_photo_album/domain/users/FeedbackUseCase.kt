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

package com.example.useful_photo_album.domain.users

import com.example.useful_photo_album.data.feedback.FeedbackEndpoint
import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.UseCase
import com.example.useful_photo_album.domain.userevent.SessionAndUserEventRepository
import com.example.useful_photo_album.shared.model.temp.SessionId
import com.example.useful_photo_album.shared.model.temp.userdata.UserEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class FeedbackUseCase @Inject constructor(
    private val endpoint: FeedbackEndpoint,
    private val repository: SessionAndUserEventRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<FeedbackParameter, Unit>(dispatcher) {

    override suspend fun execute(parameters: FeedbackParameter) {
        endpoint.sendFeedback(parameters.sessionId, parameters.responses)
        repository.recordFeedbackSent(parameters.userId, parameters.userEvent)
    }
}

data class FeedbackParameter(
    val userId: String,
    val userEvent: UserEvent,
    val sessionId: SessionId,
    val responses: Map<String, Int>
)
