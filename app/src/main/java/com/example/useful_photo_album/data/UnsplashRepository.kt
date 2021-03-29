package com.example.useful_photo_album.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.useful_photo_album.api.UnsplashService
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val service: UnsplashService) {

    /**
     * 저는 Paging 사용할때 이런식으로 api 별 sealed class 를 하나씩 만들어서 사용해요
     * 각 api 조회하는 상황에 따라 별도로 처리를 할 수도있고,, 여러모로 sealed class 가 관리는 편한 것 같네요.
     * 이부분은 정답은 없을 것 같아..ㅎ
     */
    sealed class QueryType {
        open var page: Int = 0
        open var size: Int = 0

        object Random : QueryType()
        data class Search(val query: String) : QueryType()
    }

    fun getResult(type: QueryType): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = when (type) {
                    QueryType.Random -> NETWORK_COUNT
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

    @Deprecated("replace to getResult()")
    fun getSearchResultStream(query: String): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
//            pagingSou서rceFactory = { UnsplashPagingSource(service, query) }
            //QueryType 을 Random/Search 입력받아서 넘깁니다. Search 의 경우 query: String 이 추가로 필요해서 넘겨줬습니다.
            pagingSourceFactory = { UnsplashPagingSource(service, QueryType.Search(query)) }
        ).flow
    }

    companion object {
        const val NETWORK_COUNT = 30
        private const val refreshIntervalMs: Long = 5000

        private const val NETWORK_PAGE_SIZE = 25
    }
}