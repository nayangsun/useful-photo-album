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

import androidx.core.os.trace
import com.example.useful_photo_album.di.IoDispatcher
import com.example.useful_photo_album.domain.FlowUseCase
import com.example.useful_photo_album.domain.filters.UserSessionFilterMatcher
import com.example.useful_photo_album.domain.userevent.SessionAndUserEventRepository
import com.example.useful_photo_album.shared.model.temp.filters.Filter
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.result.Result.Error
import com.example.useful_photo_album.shared.result.Result.Loading
import com.example.useful_photo_album.shared.result.Result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class SessionSearchUseCaseParams(
    val userId: String?,
    val query: String,
    val filters: List<Filter>
)

class SessionSearchUseCase @Inject constructor(
    private val repository: SessionAndUserEventRepository,
    private val textMatchStrategy: SessionTextMatchStrategy,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<SessionSearchUseCaseParams, List<UserSession>>(dispatcher) {

    override fun execute(parameters: SessionSearchUseCaseParams): Flow<Result<List<UserSession>>> {
        val (userId, query, filters) = parameters
        trace("search-path-usecase") {
            val filterMatcher = UserSessionFilterMatcher(filters)
            return repository.getObservableUserEvents(userId).map { result ->
                when (result) {
                    is Success -> {
                        val searchResults = textMatchStrategy.searchSessions(
                            result.data.userSessions, query
                        ).filter { filterMatcher.matches(it) }
                        Success(searchResults)
                    }
                    is Loading -> result
                    is Error -> result
                }
            }
        }
    }
}
