/*
 * Copyright 2020 Google LLC
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

package com.example.useful_photo_album.domain.search

import com.example.useful_photo_album.data.ConferenceDataRepository
import com.example.useful_photo_album.data.tag.TagRepository
import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.UseCase
import com.example.useful_photo_album.shared.model.temp.Tag
import com.example.useful_photo_album.shared.model.temp.filters.Filter
import com.example.useful_photo_album.shared.model.temp.filters.Filter.DateFilter
import com.example.useful_photo_album.shared.model.temp.filters.Filter.TagFilter
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

private val FILTER_CATEGORIES = listOf(
    Tag.CATEGORY_TYPE,
    Tag.CATEGORY_TOPIC,
    Tag.CATEGORY_LEVEL
)

/** Loads filters for the Search screen. */
class LoadSearchFiltersUseCase @Inject constructor(
    private val conferenceRepository: ConferenceDataRepository,
    private val tagRepository: TagRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, List<Filter>>(dispatcher) {

    override suspend fun execute(parameters: Unit): List<Filter> {
        val filters = mutableListOf<Filter>()
        filters.addAll(conferenceRepository.getConferenceDays().map { DateFilter(it) })
        filters.addAll(
            tagRepository.getTags()
                .filter { it.category in FILTER_CATEGORIES }
                .sortedWith(
                    compareBy({ FILTER_CATEGORIES.indexOf(it.category) }, { it.orderInCategory })
                )
                .map { TagFilter(it) }
        )
        return filters
    }
}
