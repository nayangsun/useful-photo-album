package com.example.useful_photo_album.data.di

import com.example.useful_photo_album.data.api.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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