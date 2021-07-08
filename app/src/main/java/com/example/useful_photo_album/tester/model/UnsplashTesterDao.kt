package com.example.useful_photo_album.tester.model

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface UnsplashTesterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnsplashes(unsplash: List<UnsplashTester>)

    @Query("SELECT * FROM unsplash")
    fun postUnsplashes(): PagingSource<Int, UnsplashTester>

    @Query("DELETE FROM unsplash")
    fun deleteAll()
}