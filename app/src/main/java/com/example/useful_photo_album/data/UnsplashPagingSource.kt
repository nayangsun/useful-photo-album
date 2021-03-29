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

    // int == totalPages
    /**
     * 이부분에서 입력받은 QueryType 별로 호출하는 api 를 분기하시면 됩니다.
     * 제가 정확한 호출방식은 몰라서 대충 코드보고 결과값만 맞춰만 놨습니다. 참고하셔서 수정해보시면 될 것 같아요.
     * 추후에는 이부분 Result 를 sealed class 로 만들어도 괜찮을 거 같네요.(nextKey, prevKey, data 등등을 포함해서 wrapping)
     */
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