package com.example.useful_photo_album.domain.entity.unsplash

data class UnsplashSearchResponse(
    val results: List<UnsplashPhoto>,
    val totalPages: Int
)
