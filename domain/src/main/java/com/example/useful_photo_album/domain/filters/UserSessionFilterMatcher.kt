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

package com.example.useful_photo_album.domain.filters

import com.example.useful_photo_album.shared.model.temp.filters.Filter
import com.example.useful_photo_album.shared.model.temp.filters.Filter.DateFilter
import com.example.useful_photo_album.shared.model.temp.filters.Filter.MyScheduleFilter
import com.example.useful_photo_album.shared.model.temp.filters.Filter.TagFilter
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession


class UserSessionFilterMatcher(filters: List<Filter>) {
    private val mySchedule = MyScheduleFilter in filters
    private val days = filters.filterIsInstance<DateFilter>().map { it.day }
    private val tagsByCategory =
        filters.filterIsInstance<TagFilter>().map { it.tag }.groupBy { it.category }

    internal fun matches(userSession: UserSession): Boolean {
        if (mySchedule && !userSession.userEvent.isStarredOrReserved()) {
            return false
        }
        if (days.isNotEmpty() && days.none { day -> userSession.session in day }) {
            return false
        }
        // For each category, session must have a tag that matches.
        val sessionTags = userSession.session.tags
        tagsByCategory.forEach { (_, tagsInCategory) ->
            if (sessionTags.intersect(tagsInCategory).isEmpty()) {
                return false
            }
        }
        return true
    }
}
