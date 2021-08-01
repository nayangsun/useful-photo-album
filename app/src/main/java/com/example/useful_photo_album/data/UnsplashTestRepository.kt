package com.example.useful_photo_album.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.useful_photo_album.AppExecutors
import com.example.useful_photo_album.api.UnsplashService
import com.example.useful_photo_album.data.remote.Resource
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import com.example.useful_photo_album.tester.model.UnsplashRandomPhoto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnsplashTestRepository @Inject constructor(
    private val service: UnsplashService,
    private val appExecutors: AppExecutors
) {

    sealed class QueryType {
        object Random : QueryType()
        data class Search(val query: String) : QueryType()
    }

    fun getResult(type: UnsplashRepository.QueryType): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = when (type) {
                    is UnsplashRepository.QueryType.Random -> NETWORK_COUNT
                    is UnsplashRepository.QueryType.Search -> NETWORK_PAGE_SIZE
                }
            ),
            pagingSourceFactory = { UnsplashPagingSource(service, type) }
        ).flow
    }

    @Deprecated("replace to getResult()")
    fun getRandomResultStream(): Flow<List<UnsplashPhoto>> = flow {
        val response = service.searchRandomPhotos(NETWORK_COUNT)
        emit(response)
        delay(refreshIntervalMs)
    }


    fun loadSearch(query: String): LiveData<Resource<PagingData<UnsplashPhoto>>> {
        return object : NetworkBoundResource<PagingData<UnsplashPhoto>, List<UnsplashPhoto>>(appExecutors) {
            override fun shouldFetch(data: PagingData<UnsplashPhoto>?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<PagingData<UnsplashPhoto>> {
                val value =  MutableLiveData<PagingData<UnsplashPhoto>>()
                value.value = null
                return value
            }
            override fun createCallTest() = getResult(UnsplashRepository.QueryType.Search(query = query)).asLiveData()
        }.asLiveData()
    }

    fun loadRandom(): LiveData<Resource<PagingData<UnsplashPhoto>>> {
        return object : NetworkBoundResource<PagingData<UnsplashPhoto>, List<UnsplashPhoto>>(appExecutors) {
            override fun shouldFetch(data: PagingData<UnsplashPhoto>?): Boolean {
                return data == null
            }
            override fun loadFromDb(): LiveData<PagingData<UnsplashPhoto>> {
                val value =  MutableLiveData<PagingData<UnsplashPhoto>>()
                value.value = null
                return value
            }
            override fun createCallTest() = getResult(UnsplashRepository.QueryType.Random).asLiveData()
        }.asLiveData()
    }

    fun loadRandomWithoutPaging(): LiveData<Resource<List<UnsplashPhoto>>> {
        return object : NetworkBoundResource<List<UnsplashPhoto>, List<UnsplashPhoto>>(appExecutors) {
            override fun shouldFetch(data: List<UnsplashPhoto>?): Boolean {
                return data == null || data.isEmpty()
            }
            override fun loadFromDb(): LiveData<List<UnsplashPhoto>> {
                val value =  MutableLiveData<List<UnsplashPhoto>>()
                value.value = null
                return value
            }
            override fun createCallTest() = getRandomResultStream().asLiveData()
        }.asLiveData()
    }

    companion object {
        const val NETWORK_COUNT = 30
        private const val refreshIntervalMs: Long = 5000
        private const val NETWORK_PAGE_SIZE = 25
    }
}