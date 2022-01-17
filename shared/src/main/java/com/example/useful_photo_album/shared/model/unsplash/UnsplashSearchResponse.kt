package com.example.useful_photo_album.shared.model.unsplash

data class UnsplashSearchResponse(
    val results: List<UnsplashPhoto>,
    val totalPages: Int
)
