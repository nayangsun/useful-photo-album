package com.example.useful_photo_album.domain.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.farmsplan.app.data.model.notification.NotificationInfo
import com.farmsplan.app.data.repository.NotificationRepository

private const val NOTIFICATION_STARTING_PAGE_INDEX = 1

class NotificationPagingSource(
    private val repository: NotificationRepository,
    private val token: String
) : PagingSource<Int, NotificationInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationInfo> {
        val page = params.key ?: NOTIFICATION_STARTING_PAGE_INDEX
        return try {
            val response = repository.searchNotification(token, page, params.loadSize, false)
            val notifications = response.data.map {
                NotificationInfo.fromResponse(it)
            }
            LoadResult.Page(
                data = notifications,
                prevKey = if (page == NOTIFICATION_STARTING_PAGE_INDEX) null else page -1,
                nextKey = if (page == response.totalPages) null else page +1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NotificationInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
