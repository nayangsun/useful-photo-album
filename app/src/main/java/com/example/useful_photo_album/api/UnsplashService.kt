package com.example.useful_photo_album.api

import com.example.useful_photo_album.BuildConfig
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import com.example.useful_photo_album.data.remote.UnsplashSearchResponse
import com.example.useful_photo_album.tester.model.UnsplashRandomPhoto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashService {

    @GET("photos/random")
    suspend fun searchRandomPhotos(
            @Query("count") count: Int,
            @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): List<UnsplashPhoto>

    @GET("photos/random")
    suspend fun searchRandomPhotosForPaging(
        @Query("count") count: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): List<UnsplashRandomPhoto>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchResponse

    companion object {
        private const val BASE_URL = "https://api.unsplash.com/"

        fun create(): UnsplashService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UnsplashService::class.java)
        }
    }
}