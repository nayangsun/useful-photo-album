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

package com.example.useful_photo_album.data.network

import com.example.useful_photo_album.shared.UpaJson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object ApiFactory {
    fun withClientAndAdapter(
        url: String,
        clientBuilder: OkHttpClient.Builder,
        factory: CallAdapter.Factory? = null
    ): Retrofit {
        val builder = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(UpaJson.base))
            .client(clientBuilder.build())
        factory?.let {
            builder.addCallAdapterFactory(it)
        }
        return builder.build()
    }

    val loggingInterceptor by lazy {
        val interceptor = HttpLoggingInterceptor { message ->
            Timber.i(message)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return@lazy interceptor
    }

    val upaApi by lazy {
        withClientAndAdapter(
            "https://api.unsplash.com/",
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
        )
    }
}
