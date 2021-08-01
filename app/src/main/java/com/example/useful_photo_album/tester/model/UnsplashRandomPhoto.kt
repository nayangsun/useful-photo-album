package com.example.useful_photo_album.tester.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.useful_photo_album.data.remote.UnsplashPhotoUrls
import com.example.useful_photo_album.data.remote.UnsplashUser
import com.google.gson.annotations.SerializedName

@Entity(tableName = "unsplash")
data class UnsplashRandomPhoto (
    @PrimaryKey
    @field:SerializedName("id") val id: String,
    @field:SerializedName("urls")
    @field:Embedded(prefix = "urls_") val urls: UnsplashPhotoUrls,
    @field:SerializedName("user_")
    @field:Embedded(prefix = "user") val user: UnsplashUser
)