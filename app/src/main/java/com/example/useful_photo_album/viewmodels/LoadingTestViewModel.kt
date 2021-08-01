package com.example.useful_photo_album.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.example.useful_photo_album.LoadingTestFragment
import com.example.useful_photo_album.data.UnsplashTestRepository
import com.example.useful_photo_album.data.remote.Resource
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The ViewModel for [LoadingTestFragment].
 */
@HiltViewModel
class LoadingTestViewModel @Inject constructor(
    private val repository: UnsplashTestRepository
) : ViewModel() {

    private val _query = MutableLiveData<String>()
    val query : LiveData<String> = _query

    private var currentRandomResult: Flow<List<UnsplashPhoto>>? = null

    val results: LiveData<Resource<List<UnsplashPhoto>>> = repository.loadRandomWithoutPaging()

    val searchResults: LiveData<Resource<PagingData<UnsplashPhoto>>> = _query.switchMap { search ->
        when (search) {
            "random" -> {
                repository.loadRandom()
            }
            else -> {
                repository.loadSearch(search)
            }
        }
    }

    fun setQuery(input: String) {
        _query.value = input
    }

}