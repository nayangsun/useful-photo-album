package com.example.useful_photo_album.data

import com.example.useful_photo_album.data.model.PhotoThemeDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoThemeRepository @Inject constructor(
    private val photoThemeDao: PhotoThemeDao)
{

    fun getThemes() = photoThemeDao.getThemes()
    fun getTheme(themeId: Int) = photoThemeDao.getTheme(themeId)
    fun getThemesWithFavoriteNumber(favoriteNumber: Int)
        = photoThemeDao.getThemeWithFavoriteNumber(favoriteNumber)
}