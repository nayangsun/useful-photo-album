package com.example.useful_photo_album.shared.model

data class UnsplashSearchResponse(
    val results: List<UnsplashPhoto>,
    val totalPages: Int
)
