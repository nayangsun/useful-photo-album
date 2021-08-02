package com.example.useful_photo_album.di

import android.content.Context
import com.example.useful_photo_album.data.model.AppDatabase
import com.example.useful_photo_album.data.model.PhotoThemeDao
import com.example.useful_photo_album.data.model.PhotoWriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePhotoWriteDao(appDatabase: AppDatabase): PhotoWriteDao {
        return appDatabase.photoWriteDao()
    }

    @Provides
    fun providePhotoThemeDao(appDatabase: AppDatabase): PhotoThemeDao {
        return appDatabase.photoThemeDao()
    }

}