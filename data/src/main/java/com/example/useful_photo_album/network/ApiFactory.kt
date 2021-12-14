package com.example.useful_photo_album.network

import com.example.useful_photo_album.common.UpaJson
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