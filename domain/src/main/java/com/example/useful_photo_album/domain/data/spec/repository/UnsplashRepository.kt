package com.example.useful_photo_album.domain.data.spec.repository

import com.example.useful_photo_album.domain.entity.unsplash.UnsplashPhoto
import com.example.useful_photo_album.domain.entity.unsplash.UnsplashSearchResponse


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