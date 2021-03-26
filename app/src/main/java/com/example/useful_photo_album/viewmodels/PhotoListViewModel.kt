package com.example.useful_photo_album.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.useful_photo_album.PhotoListFragment
import com.example.useful_photo_album.data.UnsplashRepository
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The ViewModel for [PhotoListFragment].
 */
@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {
    private var currentRandomResult: Flow<List<UnsplashPhoto>>? = null

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<UnsplashPhoto>>? = null

    fun randomPictures(): Flow<List<UnsplashPhoto>> {
        val newResult: Flow<List<UnsplashPhoto>> =
            repository.getRandomResultStream()
        currentRandomResult = newResult
        return newResult
    }

    fun searchPictures(queryString: String): Flow<PagingData<UnsplashPhoto>> {
        currentQueryValue = queryString
        val newResult: Flow<PagingData<UnsplashPhoto>> =
            repository.getSearchResultStream(queryString).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}