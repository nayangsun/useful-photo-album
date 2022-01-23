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

package com.example.useful_photo_album.presentation.signin

import com.example.useful_photo_album.di.ApplicationScope
import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.di.MainDispatcher
import com.example.useful_photo_album.domain.auth.ObserveUserAuthStateUseCase
import com.example.useful_photo_album.domain.prefs.NotificationsPrefIsShownUseCase
import com.example.useful_photo_album.shared.di.ReservationEnabledFlag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SignInViewModelDelegateModule {

    @Singleton
    @Provides
    fun provideSignInViewModelDelegate(
        dataSource: ObserveUserAuthStateUseCase,
        notificationsPrefIsShownUseCase: NotificationsPrefIsShownUseCase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        @ReservationEnabledFlag isReservationEnabledByRemoteConfig: Boolean,
        @ApplicationScope applicationScope: CoroutineScope
    ): SignInViewModelDelegate {
        return FirebaseSignInViewModelDelegate(
            observeUserAuthStateUseCase = dataSource,
            notificationsPrefIsShownUseCase = notificationsPrefIsShownUseCase,
            ioDispatcher = ioDispatcher,
            mainDispatcher = mainDispatcher,
            isReservationEnabledByRemoteConfig = isReservationEnabledByRemoteConfig,
            applicationScope = applicationScope
        )
    }
}
