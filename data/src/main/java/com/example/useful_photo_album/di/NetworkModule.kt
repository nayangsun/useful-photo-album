package com.example.useful_photo_album.di

import com.example.useful_photo_album.api.UnsplashApi
import com.example.useful_photo_album.network.ApiFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideUnsplashService(): UnsplashApi {
        return ApiFactory.upaApi.create(UnsplashApi::class.java)
    }
}