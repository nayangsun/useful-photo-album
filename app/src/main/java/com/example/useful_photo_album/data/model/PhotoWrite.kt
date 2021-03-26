package com.example.useful_photo_album.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "photos")
data class PhotoWrite (
        @ColumnInfo(name = "photo_id") val photoId: String,
        val title: String,
        val text: String,
        val imageUrl: String = "",
        @ColumnInfo(name = "photo_date") val photoDate: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var photoWriteId: Long = 0
}