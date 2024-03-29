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

package com.example.useful_photo_album.presentation.sessiondetail

import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.sessions.LoadUserSessionUseCase
import com.example.useful_photo_album.domain.users.FeedbackParameter
import com.example.useful_photo_album.domain.users.FeedbackUseCase
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.result.Result.Success
import com.example.useful_photo_album.shared.result.data
import com.example.useful_photo_album.presentation.signin.SignInViewModelDelegate
import com.example.useful_photo_album.shared.model.temp.SessionId
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.util.cancelIfActive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionFeedbackViewModel @Inject constructor(
    private val signInViewModelDelegate: SignInViewModelDelegate,
    private val loadUserSessionUseCase: LoadUserSessionUseCase,
    private val feedbackUseCase: FeedbackUseCase
) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate {

    companion object {
        val MESSAGES = mapOf(
            "q1" to Triple(
                R.string.feedback_q1_text,
                R.string.feedback_q1_label_start,
                R.string.feedback_q1_label_end
            ),
            "q2" to Triple(
                R.string.feedback_q2_text,
                R.string.feedback_q2_label_start,
                R.string.feedback_q2_label_end
            ),
            "q3" to Triple(
                R.string.feedback_q3_text,
                R.string.feedback_q3_label_start,
                R.string.feedback_q3_label_end
            ),
            "q4" to Triple(
                R.string.feedback_q4_text,
                R.string.feedback_q4_label_start,
                R.string.feedback_q4_label_end
            )
        )
    }

    private var loadUserSessionJob: Job? = null

    private var _sessionId: SessionId? = null

    private val _userSession = MutableStateFlow<Result<UserSession>>(Result.Loading)
    val userSession: StateFlow<Result<UserSession>> = _userSession

    val questions =
        MESSAGES.map { (key, value) ->
            val (text, start, end) = value
            Question(key, text, 0, start, end)
        }

    fun setSessionId(sessionId: SessionId) {
        _sessionId = sessionId
        loadUserSessionJob.cancelIfActive()
        loadUserSessionJob = viewModelScope.launch {
            loadUserSessionUseCase(userIdValue to sessionId).collect { result ->
                result.data?.userSession?.let { success ->
                    _userSession.value = Success(success)
                }
            }
        }
    }

    fun submit(feedbackUpdates: Map<String, Int>) {
        val sessionId = _sessionId ?: return
        val userId = userIdValue
        val userEvent = _userSession.value.data?.userEvent
        if (userId != null && userEvent != null) {
            viewModelScope.launch {
                feedbackUseCase(
                    FeedbackParameter(
                        userId,
                        userEvent,
                        sessionId,
                        feedbackUpdates
                    )
                )
            }
        }
    }
}

/**
 * Data model for the list.
 */
data class Question(
    val key: String,
    @StringRes
    val text: Int,
    /** 0 means unrated. Actual ratings are 1-5. */
    @IntRange(from = 0, to = 5)
    val currentRating: Int,
    @StringRes
    val labelStart: Int,
    @StringRes
    val labelEnd: Int
)
