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

package com.example.useful_photo_album.data.di

import com.example.useful_photo_album.data.api.UnsplashApi
import com.example.useful_photo_album.data.unsplash.UnsplashRepository
import com.example.useful_photo_album.data.unsplash.UnsplashRepositoryImpl
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
