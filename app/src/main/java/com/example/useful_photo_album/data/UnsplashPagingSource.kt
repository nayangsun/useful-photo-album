package com.example.useful_photo_album.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.useful_photo_album.api.UnsplashService
import com.example.useful_photo_album.data.remote.UnsplashPhoto

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val service: UnsplashService,
    private val type: UnsplashRepository.QueryType
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            type.page = page
            type.size = params.loadSize

            //val result = queryFactory()
            val result = queryResponse(page, params.loadSize)

            LoadResult.Page(
                data = result.data,
                prevKey = result.prevKey,
                nextKey = result.nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    data class QueryResult(val data: List<UnsplashPhoto>, val prevKey: Int?, val nextKey: Int?)

    private suspend fun queryResponse(page: Int, size: Int): QueryResult {
        val prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1
        return when (type) {
            is UnsplashRepository.QueryType.Random -> {
                val response = service.searchRandomPhotos(size)
                QueryResult(response, prevKey, null)
            }
            is UnsplashRepository.QueryType.Search -> {
                val response = service.searchPhotos(type.query, page, size)
                val nextKey = if (page == response.totalPages) null else page + 1
                QueryResult(response.results, prevKey, nextKey)
            }
        }
    }


    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        TODO("Not yet implemented")
    }


    /*
    @Deprecated("replace to queryResponse()")
    private suspend fun queryFactory(): Pair<List<UnsplashPhoto>, Int> {
        return when (type) {
            is UnsplashRepository.QueryType.Random -> {
                val response = service.searchRandomPhotos(UnsplashRepository.NETWORK_COUNT)
                response to 0
            }
            is UnsplashRepository.QueryType.Search -> {
                val response = service.searchPhotos(type.query, type.page, type.size)
                response.results to response.totalPages
            }
        }
    }
     */
}