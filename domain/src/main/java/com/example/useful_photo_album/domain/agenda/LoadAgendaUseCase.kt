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

package com.example.useful_photo_album.domain.agenda

import com.example.useful_photo_album.data.agenda.AgendaRepository
import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.UseCase
import com.example.useful_photo_album.shared.model.temp.Block
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Loads the agenda. When the parameter is passed as true, it's guaranteed the data
 * loaded from this use case is up to date with the remote data source (Remote Config)
 */
open class LoadAgendaUseCase @Inject constructor(
    private val repository: AgendaRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Boolean, List<Block>>(ioDispatcher) {

    override suspend fun execute(parameters: Boolean): List<Block> =
        repository.getAgenda(parameters)
            .filterNot { it.startTime == it.endTime }
            .distinct()
}
