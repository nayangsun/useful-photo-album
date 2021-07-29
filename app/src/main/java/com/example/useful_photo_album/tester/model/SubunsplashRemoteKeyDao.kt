package com.example.useful_photo_album.tester.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SubunsplashRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: SubunsplashRemoteKey)

    @Query("SELECT * FROM remote_keys WHERE subunsplash = :subunsplash")
    suspend fun remoteKeyByPost(subunsplash: String): SubunsplashRemoteKey

    @Query("DELETE FROM remote_keys WHERE subunsplash = :subunsplash")
    suspend fun deleteBysubunsplash(subunsplash: String)
}