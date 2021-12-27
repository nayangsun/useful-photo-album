package com.example.useful_photo_album.shared.model

data class UnsplashPhoto (
    val id: String,
    val urls: UnsplashPhotoUrls,
    val user: UnsplashUser
)