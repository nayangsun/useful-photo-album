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

package com.example.useful_photo_album.presentation.speaker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.sessions.LoadUserSessionsUseCase
import com.example.useful_photo_album.domain.settings.GetTimeZoneUseCase
import com.example.useful_photo_album.domain.speakers.LoadSpeakerUseCase
import com.example.useful_photo_album.domain.speakers.LoadSpeakerUseCaseResult
import com.example.useful_photo_album.presentation.sessioncommon.OnSessionStarClickDelegate
import com.example.useful_photo_album.presentation.signin.SignInViewModelDelegate
import com.example.useful_photo_album.shared.model.temp.Speaker
import com.example.useful_photo_album.shared.model.temp.SpeakerId
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.result.Result.Loading
import com.example.useful_photo_album.shared.result.data
import com.example.useful_photo_album.shared.result.successOr
import com.example.useful_photo_album.shared.util.TimeUtils
import com.example.useful_photo_album.presentation.util.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import org.threeten.bp.ZoneId
import javax.inject.Inject

/**
 * Loads a [Speaker] and their sessions, handles event actions.
 */
@HiltViewModel
class SpeakerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadSpeakerUseCase: LoadSpeakerUseCase,
    private val loadSpeakerSessionsUseCase: LoadUserSessionsUseCase,
    getTimeZoneUseCase: GetTimeZoneUseCase,
    signInViewModelDelegate: SignInViewModelDelegate,
    private val onSessionStarClickDelegate: OnSessionStarClickDelegate
) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate,
    OnSessionStarClickDelegate by onSessionStarClickDelegate {

    // TODO: remove hardcoded string when https://issuetracker.google.com/136967621 is available
    private val speakerId: SpeakerId? = savedStateHandle.get<SpeakerId>("speaker_id")

    private val loadSpeakerUseCaseResult: StateFlow<Result<LoadSpeakerUseCaseResult>> =
        flow {
            speakerId?.let { emit(loadSpeakerUseCase(speakerId)) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, Loading)

    val speakerUserSessions: StateFlow<List<UserSession>> =
        loadSpeakerUseCaseResult.transformLatest { speaker ->
            speaker.data?.let {
                loadSpeakerSessionsUseCase(it.speaker.id to it.sessionIds).collect {
                    it.data?.let { data ->
                        emit(data)
                    }
                }
            }
        }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val speaker: StateFlow<Speaker?> = loadSpeakerUseCaseResult.mapLatest {
        it.data?.speaker
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    val hasNoProfileImage: StateFlow<Boolean> = loadSpeakerUseCaseResult.mapLatest {
        it.data?.speaker?.imageUrl.isNullOrEmpty()
    }.stateIn(viewModelScope, WhileViewSubscribed, true)

    // Exposed to the view as a StateFlow but it's a one-shot operation.
    val timeZoneId = flow<ZoneId> {
        if (getTimeZoneUseCase(Unit).successOr(true)) {
            emit(TimeUtils.CONFERENCE_TIMEZONE)
        } else {
            emit(ZoneId.systemDefault())
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, TimeUtils.CONFERENCE_TIMEZONE)
}
