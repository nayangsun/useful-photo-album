package com.example.useful_photo_album.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.useful_photo_album.api.UnsplashService
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnsplashRepository @Inject constructor(
    private val service: UnsplashService,
) {

    sealed class QueryType {
        var page: Int = 0
        var size: Int = 0
        object Random : QueryType()
        data class Search(val query: String) : QueryType()
    }

    fun getResult(type: QueryType): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = when (type) {
                    is QueryType.Random -> NETWORK_COUNT
                    is QueryType.Search -> NETWORK_PAGE_SIZE
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

    companion object {
        const val NETWORK_COUNT = 30
        private const val refreshIntervalMs: Long = 5000
        private const val NETWORK_PAGE_SIZE = 25
    }
}