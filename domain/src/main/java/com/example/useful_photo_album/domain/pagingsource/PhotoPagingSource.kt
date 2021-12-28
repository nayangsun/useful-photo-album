package com.example.useful_photo_album.domain.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.useful_photo_album.domain.repository.UnsplashRepository
import com.example.useful_photo_album.shared.model.UnsplashPhoto

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class PhotoPagingSource(
    private val repository: UnsplashRepository,
    private val type: QueryType
) : PagingSource<Int, UnsplashPhoto>() {

    sealed class QueryType {
        object Random : QueryType()
        data class Search(val query: String) : QueryType()
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = queryResponse(page, params)
            val photos = response.results
            LoadResult.Page(
                data = photos,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    /**
     * 우선 캐싱 하기 전에 QueryResponse 동일하게 하여 임시로 분기
     */
    data class QueryResponse(val results: List<UnsplashPhoto>, val totalPages: Int)

    private suspend fun queryResponse(page: Int, params: LoadParams<Int>): QueryResponse {
        return when (type) {
            is QueryType.Random -> {
                val response = repository.getRandomPhotos(RANDOM_PHOTO_COUNTS)
                QueryResponse(response, 1)
            }
            is QueryType.Search -> {
                val response = repository.getSearchPhotos(type.query, page, params.loadSize)
                QueryResponse(response.results, response.totalPages)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    companion object {
        const val RANDOM_PHOTO_COUNTS = 30
    }
}
