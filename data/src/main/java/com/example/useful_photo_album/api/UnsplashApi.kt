package com.example.useful_photo_album.api

import com.example.useful_photo_album.BuildConfig
import com.example.useful_photo_album.model.UnsplashPhoto
import com.example.useful_photo_album.model.UnsplashSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos/random")
    suspend fun searchRandomPhotos(
            @Query("count") count: Int,
            @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): List<UnsplashPhoto>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchResponse
}