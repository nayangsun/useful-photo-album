/*
 * Copyright 2022 Malgeon
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

package com.example.useful_photo_album.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.iosched.ui.signin.SignInViewModelDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    signInViewModelDelegate: SignInViewModelDelegate,
    @ApplicationContext context: Context
) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate {


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
