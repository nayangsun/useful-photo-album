package com.example.useful_photo_album.tester.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class SubunsplashRemoteKey (
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val subunsplash: String, // technically mutable but fine for a demo
    val nextPageKey: String?
)