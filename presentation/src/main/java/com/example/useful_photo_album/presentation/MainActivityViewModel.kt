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

package com.example.useful_photo_album.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.ar.LoadArDebugFlagUseCase
import com.example.useful_photo_album.domain.sessions.LoadPinnedSessionsJsonUseCase
import com.google.ar.core.ArCoreApk
import com.example.useful_photo_album.presentation.signin.SignInViewModelDelegate
import com.example.useful_photo_album.presentation.theme.ThemedActivityDelegate
import com.example.useful_photo_album.presentation.util.WhileViewSubscribed
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.result.data
import com.example.useful_photo_album.shared.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    signInViewModelDelegate: SignInViewModelDelegate,
    themedActivityDelegate: ThemedActivityDelegate,
    loadPinnedSessionsUseCase: LoadPinnedSessionsJsonUseCase,
    loadArDebugFlagUseCase: LoadArDebugFlagUseCase,
    @ApplicationContext context: Context
) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate,
    ThemedActivityDelegate by themedActivityDelegate {

    private val _navigationActions = Channel<MainNavigationAction>(Channel.CONFLATED)
    val navigationActions = _navigationActions.receiveAsFlow()

    val pinnedSessionsJson: StateFlow<String> = userInfo.transformLatest { user ->
        val uid = user?.getUid()
        if (uid != null) {
            loadPinnedSessionsUseCase(uid).collect { result ->
                if (result is Result.Success) {
                    emit(result.data)
                }
            }
        } else {
            emit("")
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, "")

    val canSignedInUserDemoAr: StateFlow<Boolean> = userInfo.transformLatest {
        val result = loadArDebugFlagUseCase(Unit)
        if (result is Result.Success) {
            emit(result.data)
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    val arCoreAvailability: StateFlow<ArCoreApk.Availability?> = flow<ArCoreApk.Availability> {
        var result: ArCoreApk.Availability? = null
        while (result == null) {
            val availability = ArCoreApk.getInstance().checkAvailability(context)
            // If the availability is transient, we need to call availability check again
            // as in https://developers.google.com/ar/develop/java/enable-arcore#check_supported
            if (availability.isTransient) {
                delay(1000)
            } else {
                result = availability
                emit(result)
            }
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    fun onProfileClicked() {
        if (isUserSignedInValue) {
            _navigationActions.tryOffer(MainNavigationAction.OpenSignOut)
        } else {
            _navigationActions.tryOffer(MainNavigationAction.OpenSignIn)
        }
    }
}

sealed class MainNavigationAction {
    object OpenSignIn : MainNavigationAction()
    object OpenSignOut : MainNavigationAction()
}
