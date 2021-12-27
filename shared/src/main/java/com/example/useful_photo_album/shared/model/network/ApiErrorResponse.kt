package com.example.useful_photo_album.shared.model.network

/**
 * API 호출 시 에러 응답
 *
 * @property errors 자세한 에러 설명
 */

data class ApiErrorResponse(
    val errors: List<String>?
) {
    val message: String = errors?.let {
        if(it.isEmpty()) "" else it[0]
    } ?: ""
}