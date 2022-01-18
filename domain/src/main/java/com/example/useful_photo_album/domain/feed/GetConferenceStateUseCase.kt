/*
 * Copyright 2020 Google LLC
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

package com.example.useful_photo_album.domain.feed

import com.example.useful_photo_album.di.MainDispatcher
import com.example.useful_photo_album.domain.FlowUseCase
import com.example.useful_photo_album.shared.util.TimeUtils
import com.example.useful_photo_album.domain.feed.ConferenceState.ENDED
import com.example.useful_photo_album.domain.feed.ConferenceState.STARTED
import com.example.useful_photo_album.domain.feed.ConferenceState.UPCOMING
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.time.TimeProvider
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Duration

enum class ConferenceState { UPCOMING, STARTED, ENDED }

/**
 * Gets the current [ConferenceState].
 */
class GetConferenceStateUseCase @Inject constructor(
    @MainDispatcher val mainDispatcher: CoroutineDispatcher,
    private val timeProvider: TimeProvider
) : FlowUseCase<Unit?, ConferenceState>(mainDispatcher) {

    override fun execute(parameters: Unit?): Flow<Result<ConferenceState>> {
        return moveToNextState().map { Result.Success(it) }
    }

    private fun moveToNextState(): Flow<ConferenceState> = flow {
        do {
            val (nextState, delayForLaterState) = getNextStateWithDelay()
            emit(nextState)
            delay(delayForLaterState ?: 0)
        } while (nextState != ENDED)
    }

    private fun getNextStateWithDelay(): Pair<ConferenceState, Long?> {
        val timeUntilStart = Duration.between(timeProvider.now(), TimeUtils.getKeynoteStartTime())
        return if (timeUntilStart.isNegative) {
            val timeUntilEnd =
                Duration.between(timeProvider.now(), TimeUtils.getConferenceEndTime())
            if (timeUntilEnd.isNegative) {
                Pair(ENDED, null)
            } else {
                Pair(STARTED, timeUntilEnd.toMillis())
            }
        } else {
            Pair(UPCOMING, timeUntilStart.toMillis())
        }
    }
}
