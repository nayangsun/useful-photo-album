package com.example.useful_photo_album.tester.model

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.useful_photo_album.data.remote.UnsplashPhoto

@Dao
interface UnsplashPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnsplash(unsplash: List<UnsplashRandomPhoto>)

    /**
     *  이곳에서 Execution failed for task ':app:kaptDebugKotlin' 이 등장합니다.
     */
    @Query("SELECT * FROM unsplash")
    fun postsUnsplash(subunsplash: String): PagingSource<Int, UnsplashRandomPhoto>

    @Query("DELETE FROM unsplash")
    suspend fun deleteAll()
}
