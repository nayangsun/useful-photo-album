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

import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.FlowUseCase
import com.example.useful_photo_album.domain.userevent.DefaultSessionAndUserEventRepository
import com.example.useful_photo_album.domain.userevent.UserEventMessage
import com.example.useful_photo_album.shared.model.temp.SessionId
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.result.Result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalCoroutinesApi
open class LoadUserSessionUseCase @Inject constructor(
    private val userEventRepository: DefaultSessionAndUserEventRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Pair<String?, SessionId>, LoadUserSessionUseCaseResult>(ioDispatcher) {

    override fun execute(parameters: Pair<String?, SessionId>):
        Flow<Result<LoadUserSessionUseCaseResult>> {
            val (userId, eventId) = parameters
            return userEventRepository.getObservableUserEvent(userId, eventId).map {
                if (it is Success) {
                    Success(LoadUserSessionUseCaseResult(userSession = it.data.userSession))
                } else {
                    it
                }
            }
        }
}

data class LoadUserSessionUseCaseResult(
    val userSession: UserSession,

    /** A message to show to the user with important changes like reservation confirmations */
    val userMessage: UserEventMessage? = null
)
