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

package com.example.useful_photo_album.presentation.sessioncommon

import com.example.useful_photo_album.di.ApplicationScope
import com.example.useful_photo_album.di.MainDispatcher
import com.example.useful_photo_album.domain.users.StarEventAndNotifyUseCase
import com.example.useful_photo_album.domain.users.StarEventParameter
import com.example.useful_photo_album.presentation.R
import com.example.useful_photo_album.presentation.messages.SnackbarMessage
import com.example.useful_photo_album.presentation.messages.SnackbarMessageManager
import com.example.useful_photo_album.presentation.signin.SignInViewModelDelegate
import com.example.useful_photo_album.shared.analytics.AnalyticsActions
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.util.tryOffer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

/**
 * A delegate providing common functionality for starring events.
 */

interface OnSessionStarClickDelegate : OnSessionStarClickListener {
    val navigateToSignInDialogEvents: Flow<Unit>
}

class DefaultOnSessionStarClickDelegate @Inject constructor(
    signInViewModelDelegate: SignInViewModelDelegate,
    private val starEventUseCase: StarEventAndNotifyUseCase,
    private val snackbarMessageManager: SnackbarMessageManager,
    private val analyticsHelper: AnalyticsHelper,
    @ApplicationScope private val externalScope: CoroutineScope,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : OnSessionStarClickDelegate, SignInViewModelDelegate by signInViewModelDelegate {

    private val _navigateToSignInDialogEvents = Channel<Unit>(capacity = Channel.CONFLATED)
    override val navigateToSignInDialogEvents = _navigateToSignInDialogEvents.receiveAsFlow()

    override fun onStarClicked(userSession: UserSession) {
        if (!isUserSignedInValue) {
            Timber.d("Showing Sign-in dialog after star click")
            _navigateToSignInDialogEvents.tryOffer(Unit)
            return
        }
        val newIsStarredState = !userSession.userEvent.isStarred

        // Update the snackbar message optimistically.
        val stringResId = if (newIsStarredState) {
            R.string.event_starred
        } else {
            R.string.event_unstarred
        }
        snackbarMessageManager.addMessage(
            SnackbarMessage(
                messageId = stringResId,
                actionId = R.string.dont_show,
                requestChangeId = UUID.randomUUID().toString()
            )
        )
        if (newIsStarredState) {
            analyticsHelper.logUiEvent(userSession.session.title, AnalyticsActions.STARRED)
        }

        externalScope.launch(mainDispatcher) {
            userIdValue?.let {
                val result = starEventUseCase(
                    StarEventParameter(
                        it,
                        userSession.copy(
                            userEvent = userSession.userEvent.copy(isStarred = newIsStarredState)
                        )
                    )
                )
                // Show an error message if a star request fails
                if (result is Result.Error) {
                    snackbarMessageManager.addMessage(SnackbarMessage(R.string.event_star_error))
                }
            }
        }
    }
}
