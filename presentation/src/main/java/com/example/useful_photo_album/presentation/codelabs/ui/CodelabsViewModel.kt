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

package com.example.useful_photo_album.presentation.codelabs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.codelabs.GetCodelabsInfoCardShownUseCase
import com.example.useful_photo_album.domain.codelabs.LoadCodelabsUseCase
import com.example.useful_photo_album.domain.codelabs.SetCodelabsInfoCardShownUseCase
import com.example.useful_photo_album.presentation.codelabs.ui.adapter.CodelabsInformationCard
import com.example.useful_photo_album.shared.model.temp.Codelab
import com.example.useful_photo_album.shared.result.successOr
import com.example.useful_photo_album.presentation.util.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@HiltViewModel
class CodelabsViewModel @Inject constructor(
    loadCodelabsUseCase: LoadCodelabsUseCase,
    getCodelabsInfoCardShownUseCase: GetCodelabsInfoCardShownUseCase,
    private val setCodelabsInfoCardShownUseCase: SetCodelabsInfoCardShownUseCase
) : ViewModel() {

    private val infoCardDismissed = getCodelabsInfoCardShownUseCase(Unit).map {
        it.successOr(false)
    }

    private val codelabs = flow {
        emit(loadCodelabsUseCase(Unit).successOr(emptyList()))
    }

    val screenContent = combine(codelabs, infoCardDismissed) { list, cardDismissed ->
        buildScreenContent(list, cardDismissed)
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    private fun buildScreenContent(codelabs: List<Codelab>, cardDismissed: Boolean): List<Any> {
        val items = mutableListOf<Any>()
        if (!cardDismissed) {
            items.add(CodelabsInformationCard)
        }
        items.addAll(codelabs)
        return items
    }

    fun dismissCodelabsInfoCard() {
        viewModelScope.launch {
            setCodelabsInfoCardShownUseCase(Unit)
        }
    }
}
