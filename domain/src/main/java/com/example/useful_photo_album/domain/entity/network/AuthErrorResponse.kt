package com.example.useful_photo_album.domain.entity.network

data class AuthErrorResponse(
    val error: String,
    val errorDescription: String?
)