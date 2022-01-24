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

package com.example.useful_photo_album.presentation.reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.sessions.LoadUserSessionUseCase
import com.example.useful_photo_album.domain.users.ReservationActionUseCase
import com.example.useful_photo_album.domain.users.ReservationRequestAction
import com.example.useful_photo_album.domain.users.ReservationRequestParameters
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.messages.SnackbarMessage
import com.example.useful_photo_album.presentation.signin.SignInViewModelDelegate
import com.example.useful_photo_album.shared.model.temp.SessionId
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.result.data
import com.example.useful_photo_album.presentation.util.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoveReservationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    signInViewModelDelegate: SignInViewModelDelegate,
    private val loadUserSessionUseCase: LoadUserSessionUseCase,
    private val reservationActionUseCase: ReservationActionUseCase
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val sessionId: SessionId? = savedStateHandle.get<SessionId>("session_id")

    private val userIdStateFlow: StateFlow<String?> =
        userId.stateIn(viewModelScope, WhileViewSubscribed, null)

    val userSession: StateFlow<UserSession?> = userIdStateFlow.transform { userId ->
        if (userId != null && sessionId != null) {
            loadUserSessionUseCase(userId to sessionId).collect { loadResult ->
                emit(loadResult.data?.userSession)
            }
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val _snackbarMessages = Channel<SnackbarMessage>(3, BufferOverflow.DROP_LATEST)
    val snackbarMessages: Flow<SnackbarMessage> =
        _snackbarMessages.receiveAsFlow().shareIn(viewModelScope, WhileViewSubscribed)

    fun removeReservation() {
        if (sessionId == null) return
        val userId = userIdStateFlow.value ?: return
        val userSession = userSession.value

        viewModelScope.launch {
            val result = reservationActionUseCase(
                ReservationRequestParameters(
                    userId,
                    sessionId,
                    ReservationRequestAction.CancelAction(),
                    userSession
                )
            )
            if (result is Error) {
                _snackbarMessages.send(
                    SnackbarMessage(
                        messageId = R.string.reservation_error,
                        longDuration = true
                    )
                )
            }
        }
    }
}
