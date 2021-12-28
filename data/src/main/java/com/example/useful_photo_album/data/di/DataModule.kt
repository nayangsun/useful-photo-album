package com.example.useful_photo_album.data.di

import com.example.useful_photo_album.data.api.UnsplashApi
import com.example.useful_photo_album.data.repository.UnsplashRepositoryImpl
import com.example.useful_photo_album.domain.repository.UnsplashRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule.ApiModule::class])
internal abstract class DataModule {

    @Binds
    abstract fun bindsUnsplashRepository(
        repository: UnsplashRepositoryImpl
    ): UnsplashRepository

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