package com.example.useful_photo_album.data.unsplash

import com.example.useful_photo_album.shared.model.unsplash.UnsplashPhoto
import com.example.useful_photo_album.shared.model.unsplash.UnsplashSearchResponse


interface UnsplashRepository {
    suspend fun getSearchPhotos(
        query: String,
        page: Int,
        perPage: Int,
    ): UnsplashSearchResponse

    suspend fun getRandomPhotos(
        count: Int,
    ): List<UnsplashPhoto>
}