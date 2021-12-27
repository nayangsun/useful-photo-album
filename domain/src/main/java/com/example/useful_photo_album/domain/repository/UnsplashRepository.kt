package com.example.useful_photo_album.domain.repository

import com.example.useful_photo_album.shared.model.UnsplashSearchResponse


interface UnsplashRepository {
    suspend fun getSearch(
        query: String,
        page: Int,
        perPage: Int,
    ): UnsplashSearchResponse
}