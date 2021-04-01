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
            type.page = page
            type.size = params.loadSize


            val result = queryFactory()
            LoadResult.Page(
                data = result.first,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (type == UnsplashRepository.QueryType.Random) null
                else {
                    if (page == result.second) null else page + 1
                }
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private suspend fun queryFactory(): Pair<List<UnsplashPhoto>, Int> {
        return when (type) {
            UnsplashRepository.QueryType.Random -> {
                val response = service.searchRandomPhotos(UnsplashRepository.NETWORK_COUNT)
                response to 0
            }
            is UnsplashRepository.QueryType.Search -> {
                val response = service.searchPhotos(type.query, type.page, type.size)
                response.results to response.totalPages
            }
        }
    }

}