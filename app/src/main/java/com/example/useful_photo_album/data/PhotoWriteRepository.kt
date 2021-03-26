package com.example.useful_photo_album.data

import com.example.useful_photo_album.data.model.PhotoWrite
import com.example.useful_photo_album.data.model.PhotoWriteDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoWriteRepository @Inject constructor(
        private val photoWriteDao: PhotoWriteDao
) {
    suspend fun createPhotoWrite(photoId: String, title: String, text: String, imageUrl: String="") {
        val photoWrite = PhotoWrite(photoId, title, text, imageUrl)
        photoWriteDao.insertPhotoWrite(photoWrite)
    }

    suspend fun updatePhotoWrite(photoWrite: PhotoWrite) {
        photoWriteDao.updatePhotoWrite(photoWrite)
    }

    suspend fun removePhotoWrite(photoWrite: PhotoWrite) {
        photoWriteDao.deletePhotoWrite(photoWrite)
    }

    fun getPhotoWrites() = photoWriteDao.getPhotoTexts()

    fun getPhotoWrite(photoId: String) = photoWriteDao.getPhotoText(photoId)
}