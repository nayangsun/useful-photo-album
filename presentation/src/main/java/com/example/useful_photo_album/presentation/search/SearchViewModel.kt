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

package com.example.useful_photo_album.presentation.search

import androidx.core.os.trace
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useful_photo_album.domain.search.LoadSearchFiltersUseCase
import com.example.useful_photo_album.domain.search.SessionSearchUseCase
import com.example.useful_photo_album.domain.search.SessionSearchUseCaseParams
import com.example.useful_photo_album.domain.settings.GetTimeZoneUseCase
import com.example.useful_photo_album.presentation.filters.FiltersViewModelDelegate
import com.example.useful_photo_album.presentation.signin.SignInViewModelDelegate
import com.example.useful_photo_album.shared.analytics.AnalyticsActions
import com.example.useful_photo_album.shared.analytics.AnalyticsHelper
import com.example.useful_photo_album.shared.model.temp.userdata.UserSession
import com.example.useful_photo_album.shared.result.Result
import com.example.useful_photo_album.shared.result.Result.Loading
import com.example.useful_photo_album.shared.result.successOr
import com.example.useful_photo_album.shared.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.ZoneId
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val searchUseCase: SessionSearchUseCase,
    getTimeZoneUseCase: GetTimeZoneUseCase,
    loadFiltersUseCase: LoadSearchFiltersUseCase,
    signInViewModelDelegate: SignInViewModelDelegate,
    filtersViewModelDelegate: FiltersViewModelDelegate
) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate,
    FiltersViewModelDelegate by filtersViewModelDelegate {

    private val _searchResults = MutableStateFlow<List<UserSession>>(emptyList())
    val searchResults: StateFlow<List<UserSession>> = _searchResults

    private val _isEmpty = MutableStateFlow(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    private var searchJob: Job? = null

    val timeZoneId: StateFlow<ZoneId> = flow {
        if (getTimeZoneUseCase(Unit).successOr(true)) {
            emit(TimeUtils.CONFERENCE_TIMEZONE)
        } else {
            emit(ZoneId.systemDefault())
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ZoneId.systemDefault())

    private var textQuery = ""

    // Override because we also want to show result count when there's a text query.
    private val _showResultCount = MutableStateFlow(false)
    override val showResultCount: StateFlow<Boolean> = _showResultCount

    init {
        // Load filters
        viewModelScope.launch {
            setSupportedFilters(loadFiltersUseCase(Unit).successOr(emptyList()))
        }

        // Re-execute search when selected filters change
        viewModelScope.launch {
            selectedFilters.collect {
                executeSearch()
            }
        }

        // Re-execute search when signed in user changes.
        // Required because we show star / reservation status.
        viewModelScope.launch {
            userInfo.collect {
                executeSearch()
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val newQuery = query.trim().takeIf { it.length >= 2 } ?: ""
        if (textQuery != newQuery) {
            textQuery = newQuery
            analyticsHelper.logUiEvent("Query: $newQuery", AnalyticsActions.SEARCH_QUERY_SUBMIT)
            executeSearch()
        }
    }

    private fun executeSearch() {
        // Cancel any in-flight searches
        searchJob?.cancel()

        val filters = selectedFilters.value
        if (textQuery.isEmpty() && filters.isEmpty()) {
            clearSearchResults()
            return
        }

        searchJob = viewModelScope.launch {
            // The user could be typing or toggling filters rapidly. Giving the search job
            // a slight delay and cancelling it on each call to this method effectively debounces.
            delay(500)
            trace("search-path-viewmodel") {
                searchUseCase(
                    SessionSearchUseCaseParams(userIdValue, textQuery, filters)
                ).collect {
                    processSearchResult(it)
                }
            }
        }
    }

    private fun clearSearchResults() {
        _searchResults.value = emptyList()
        // Explicitly set false to not show the "No results" state
        _isEmpty.value = false
        _showResultCount.value = false
        resultCount.value = 0
    }

    private fun processSearchResult(searchResult: Result<List<UserSession>>) {
        if (searchResult is Loading) {
            return // avoids UI flickering
        }
        val sessions = searchResult.successOr(emptyList())
        _searchResults.value = sessions
        _isEmpty.value = sessions.isEmpty()
        _showResultCount.value = true
        resultCount.value = sessions.size
    }
}
