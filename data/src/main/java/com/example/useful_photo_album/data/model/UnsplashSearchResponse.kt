package com.example.useful_photo_album.data.model

import com.example.useful_photo_album.data.model.UnsplashPhoto
import com.google.gson.annotations.SerializedName

data class UnsplashSearchResponse(
    @field:SerializedName("results") val results: List<UnsplashPhoto>,
    @field:SerializedName("total_pages") val totalPages: Int
)
