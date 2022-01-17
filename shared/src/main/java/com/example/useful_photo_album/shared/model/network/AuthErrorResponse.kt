package com.example.useful_photo_album.shared.model.network

data class AuthErrorResponse(
    val error: String,
    val errorDescription: String?
)