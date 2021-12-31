package com.example.useful_photo_album.data.api

import com.example.useful_photo_album.domain.entity.unsplash.UnsplashPhoto
import com.example.useful_photo_album.domain.entity.unsplash.UnsplashSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos/random")
    suspend fun randomPhotos(
            @Query("count") count: Int,
    ): List<UnsplashPhoto>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): UnsplashSearchResponse
}