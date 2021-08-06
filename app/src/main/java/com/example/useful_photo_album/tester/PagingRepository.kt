package com.example.useful_photo_album.tester

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.useful_photo_album.api.UnsplashService
import com.example.useful_photo_album.data.model.AppDatabase
import com.example.useful_photo_album.tester.model.UnsplashRandomPhoto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PagingRepository @Inject constructor(
    private val service: UnsplashService,
    private val db: AppDatabase
) {
    private val pagingDao = db.posts()

    @ExperimentalPagingApi
    fun pagingUnsplash(subunsplash: String): Flow<PagingData<UnsplashRandomPhoto>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            remoteMediator = RandomRemoteMediator(db, service)
        ) {
            pagingDao.postsUnsplash(subunsplash)
        }.flow
    }
}