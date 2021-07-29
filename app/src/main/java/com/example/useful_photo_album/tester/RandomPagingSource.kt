//package com.example.useful_photo_album.tester
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.useful_photo_album.api.UnsplashService
//import com.example.useful_photo_album.data.remote.UnsplashPhoto
//
//private const val UNSPLASH_STARTING_PAGE_INDEX = 1
//private const val RANDOM_SEARCH_PAGE = 30
//
//class RandomPagingSource(
//    private val service: UnsplashService
//) : PagingSource<Int, UnsplashPhoto>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
//        return try {
//            val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
//            val response = service.searchRandomPhotos(RANDOM_SEARCH_PAGE)
//
//            LoadResult.Page(
//                data = response,
//                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
//                nextKey = null
//            )
//        } catch (exception: Exception) {
//            LoadResult.Error(exception)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}