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

import com.example.useful_photo_album.data.session.SessionRepository
import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.UseCase
import com.example.useful_photo_album.shared.model.temp.Session
import com.example.useful_photo_album.shared.model.temp.SessionId
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

open class LoadSessionOneShotUseCase @Inject constructor(
    private val repository: SessionRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<SessionId, Session>(dispatcher) {

    override suspend fun execute(parameters: SessionId): Session {
        return repository.getSession(parameters)
    }
}
