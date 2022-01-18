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

package com.example.useful_photo_album.presentation.agenda.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.agenda.LoadAgendaUseCase
import com.example.useful_photo_album.shared.model.temp.Block
import com.example.useful_photo_album.domain.settings.GetTimeZoneUseCase
import com.example.useful_photo_album.presentation.util.WhileViewSubscribed
import com.example.useful_photo_album.shared.util.TimeUtils
import com.example.useful_photo_album.shared.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.ZoneId
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val loadAgendaUseCase: LoadAgendaUseCase,
    private val getTimeZoneUseCase: GetTimeZoneUseCase
) : ViewModel() {

    // Expose agenda data
    val agenda: StateFlow<List<Block>> = flow {
        val agendaData = loadAgendaUseCase(false).data ?: emptyList()
        emit(agendaData)
    }.stateIn(viewModelScope, WhileViewSubscribed, initialValue = emptyList())

    // Expose whether we're on conference timezone or local
    val timeZoneId = flow<ZoneId> {
        if (getTimeZoneUseCase(Unit).data == true) {
            emit(TimeUtils.CONFERENCE_TIMEZONE)
        } else {
            emit(ZoneId.systemDefault())
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, initialValue = ZoneId.systemDefault())
}
