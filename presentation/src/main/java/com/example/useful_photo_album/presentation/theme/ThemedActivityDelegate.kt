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

package com.example.useful_photo_album.presentation.theme

import com.example.useful_photo_album.di.ApplicationScope
import com.example.useful_photo_album.domain.settings.GetThemeUseCase
import com.example.useful_photo_album.domain.settings.ObserveThemeModeUseCase
import com.example.useful_photo_album.shared.result.Result.Success
import com.example.useful_photo_album.shared.result.successOr
import com.google.samples.apps.iosched.model.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Interface to implement activity theming via a ViewModel.
 *
 * You can inject a implementation of this via Dagger2, then use the implementation as an interface
 * delegate to add the functionality without writing any code
 *
 * Example usage:
 * ```
 * class MyViewModel @Inject constructor(
 *     themedActivityDelegate: ThemedActivityDelegate
 * ) : ViewModel(), ThemedActivityDelegate by themedActivityDelegate {
 * ```
 */
interface ThemedActivityDelegate {
    /**
     * Allows observing of the current theme
     */
    val theme: StateFlow<Theme>

    /**
     * Allows querying of the current theme synchronously
     */
    val currentTheme: Theme
}

class ThemedActivityDelegateImpl @Inject constructor(
    @ApplicationScope externalScope: CoroutineScope,
    observeThemeUseCase: ObserveThemeModeUseCase,
    private val getThemeUseCase: GetThemeUseCase
) : ThemedActivityDelegate {

    override val theme: StateFlow<Theme> = observeThemeUseCase(Unit).map {
        it.successOr(Theme.SYSTEM)
    }.stateIn(externalScope, SharingStarted.Eagerly, Theme.SYSTEM)

    override val currentTheme: Theme
        get() = runBlocking { // Using runBlocking to execute this coroutine synchronously
            getThemeUseCase(Unit).let {
                if (it is Success) it.data else Theme.SYSTEM
            }
        }
}
