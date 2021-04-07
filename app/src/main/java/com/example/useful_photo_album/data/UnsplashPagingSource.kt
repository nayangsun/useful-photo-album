package com.example.useful_photo_album.data

import androidx.paging.PagingSource
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
            //type.page = page
            //type.size = params.loadSize

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

    /**
     * sealed class를 어떻게 이용할지 고민하다, 결국 만들어진 sealed를 이용해 data class 할당을 해서
     * 이렇게 바꿔봤는데요, load 부분에서 조금더 직관적으로 보이지 않나라는 생각을 해봅니다.
     * 그래도 sealed class로 바꿔보고 싶은데요. 혹시 간단한 힌트라도 알려주신다면 감사하겠습니다.
     */

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