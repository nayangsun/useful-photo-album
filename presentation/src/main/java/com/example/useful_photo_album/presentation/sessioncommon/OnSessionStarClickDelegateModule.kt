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
import com.example.useful_photo_album.presentation.messages.SnackbarMessageManager
import com.example.useful_photo_album.presentation.signin.SignInViewModelDelegate
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * Provides a default implementation of [OnSessionStarClickDelegate].
 */
@InstallIn(ViewModelComponent::class)
@Module
internal class OnSessionStarClickDelegateModule {

    @Provides
    fun provideOnSessionStarClickDelegate(
        signInViewModelDelegate: SignInViewModelDelegate,
        starEventUseCase: StarEventAndNotifyUseCase,
        snackbarMessageManager: SnackbarMessageManager,
        analyticsHelper: AnalyticsHelper,
        @ApplicationScope applicationScope: CoroutineScope,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): OnSessionStarClickDelegate {
        return DefaultOnSessionStarClickDelegate(
            signInViewModelDelegate,
            starEventUseCase,
            snackbarMessageManager,
            analyticsHelper,
            applicationScope,
            mainDispatcher
        )
    }
}
