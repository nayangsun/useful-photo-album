package com.example.useful_photo_album.di

import com.example.useful_photo_album.api.UnsplashApi
import com.example.useful_photo_album.network.ApiFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule.ApiModule::class])
internal abstract class DataModule {

    @InstallIn(SingletonComponent::class)
    @Module
    internal object ApiModule {

        @Singleton
        @Provides
        fun provideUnsplashService(
            retrofit: Retrofit
        ): UnsplashApi {
            return retrofit.create(UnsplashApi::class.java)
        }
    }
}