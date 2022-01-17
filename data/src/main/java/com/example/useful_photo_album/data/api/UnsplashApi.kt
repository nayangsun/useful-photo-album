/*
 * Copyright 2022 Malgeon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.useful_photo_album.data.api

import com.example.useful_photo_album.shared.model.unsplash.UnsplashPhoto
import com.example.useful_photo_album.shared.model.unsplash.UnsplashSearchResponse
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
