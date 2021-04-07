package com.example.useful_photo_album.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Tag (
    val tag: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var tagId: Long = 0
}