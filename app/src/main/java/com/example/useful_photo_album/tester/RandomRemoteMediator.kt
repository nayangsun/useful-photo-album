package com.example.useful_photo_album.tester

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.useful_photo_album.api.UnsplashService
import com.example.useful_photo_album.data.model.AppDatabase
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import com.example.useful_photo_album.tester.model.UnsplashPhotoDao
import com.example.useful_photo_album.tester.model.UnsplashRandomPhoto
import retrofit2.HttpException
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class RandomRemoteMediator (
    private val db : AppDatabase,
    private val api : UnsplashService
) : RemoteMediator<Int, UnsplashRandomPhoto>() {
    private val postDao: UnsplashPhotoDao = db.posts()

    // 간단하게 random 쿼리 30개 response 만 load 하려고 한다.
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashRandomPhoto>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val data = api.searchRandomPhotosForPaging(30)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postDao.deleteAll()
                }

                postDao.insertUnsplash(data)
            }
            return MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

}