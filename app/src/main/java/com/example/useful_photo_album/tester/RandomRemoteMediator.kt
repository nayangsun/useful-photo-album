package com.example.useful_photo_album.tester

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.useful_photo_album.api.UnsplashService
import com.example.useful_photo_album.data.model.AppDatabase
import com.example.useful_photo_album.tester.model.SubunsplashRemoteKey
import com.example.useful_photo_album.tester.model.SubunsplashRemoteKeyDao
import com.example.useful_photo_album.tester.model.UnsplashRandomPhotoDao
import com.example.useful_photo_album.tester.model.UnsplashRandomPhoto

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class RandomRemoteMediator (
    private val db : AppDatabase,
    private val api : UnsplashService
) : RemoteMediator<Int, UnsplashRandomPhoto>() {
    private val postDao: UnsplashRandomPhotoDao = db.posts()
    private val keyDao: SubunsplashRemoteKeyDao = db.remoteKeys()

    // 간단하게 random 쿼리 30개 response 만 load 하려고 한다.
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashRandomPhoto>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {

                    val lastItem = state.lastItemOrNull()
                    val remoteKey: SubunsplashRemoteKey? = db.withTransaction {
                        if (lastItem?.id != null) {
                            keyDao.remoteKeyByPost(lastItem.id)
                        } else null
                    }

                    if (remoteKey?.nextPageKey == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    val uri = Uri.parse(remoteKey.nextPageKey)
                    val nextPageQuery = uri.getQueryParameter("page")
                    nextPageQuery?.toInt()
                }
            }

            val response = api.searchRandomPhotosForPaging(loadKey ?: UNSPLASH_STARTING_PAGE_INDEX)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postDao.deleteAll()
                    keyDao.deleteAll()
                }
                response?.forEach {
                    keyDao.insert(SubunsplashRemoteKey(it.id,null))
                }

                response?.let { postDao.insertUnsplash(it) }
            }

            return MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

}