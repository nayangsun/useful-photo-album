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
    // 나중에 current값이 다룰때가 있을것 같아서 만들어 놨는데, with paging에선 일단 빼두자.
    private var currentRandomResult: Flow<List<UnsplashPhoto>>? = null

    fun randomPicturesWithPaging(): Flow<PagingData<UnsplashPhoto>> {
        return repository.getResult(UnsplashRepository.QueryType.Random).cachedIn(viewModelScope)
    }

    fun searchPicturesWithPaging(query: String): Flow<PagingData<UnsplashPhoto>> {
        return repository.getResult(UnsplashRepository.QueryType.Search(query = query)).cachedIn(viewModelScope)
    }

    @Deprecated("replace to randomPicturesWithPaging")
    fun randomPictures(): Flow<List<UnsplashPhoto>> {
        val newResult: Flow<List<UnsplashPhoto>> =
            repository.getRandomResultStream()
        currentRandomResult = newResult
        return newResult
    }

}