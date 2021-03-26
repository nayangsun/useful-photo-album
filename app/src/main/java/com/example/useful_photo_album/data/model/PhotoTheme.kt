package com.example.useful_photo_album.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "themes")
data class PhotoTheme(
    val theme: String,
    val favoriteThemeNumber: Int,
    val imageUrl: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var photoThemeId: Long = 0

    override fun toString() = theme
}
