package com.example.useful_photo_album.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.useful_photo_album.data.pref.DataStorePreferenceStorage
import com.example.useful_photo_album.domain.data.spec.datastore.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferencesStorageModule {
    val Context.dataStore by preferencesDataStore(
        name = DataStorePreferenceStorage.PREFS_NAME
    )

    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
        DataStorePreferenceStorage(context.dataStore)
}