package com.example.useful_photo_album.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoWriteDao {
    @Query("SELECT * FROM photos")
    fun getPhotoTexts(): Flow<List<PhotoWrite>>

    @Query("SELECT * FROM photos WHERE photo_id = :photoId")
    fun getPhotoText(photoId: String): Flow<PhotoWrite>

    @Insert
    suspend fun insertPhotoWrite(photoWrite: PhotoWrite): Long

    @Update
    suspend fun updatePhotoWrite(photoWrite: PhotoWrite)

    @Delete
    suspend fun deletePhotoWrite(photoWrite: PhotoWrite)
}