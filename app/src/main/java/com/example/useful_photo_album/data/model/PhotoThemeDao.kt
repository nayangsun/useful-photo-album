package com.example.useful_photo_album.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoThemeDao {
    @Query("SELECT * FROM themes")
    fun getThemes(): Flow<List<PhotoTheme>>

    @Query("SELECT * FROM themes WHERE favoriteThemeNumber = :favoriteThemeNumber")
    fun getThemeWithFavoriteNumber(favoriteThemeNumber: Int): Flow<List<PhotoTheme>>

    @Query("SELECT * FROM themes WHERE id = :themeId")
    fun getTheme(themeId: Int): Flow<PhotoTheme>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(themes: List<PhotoTheme>)
}