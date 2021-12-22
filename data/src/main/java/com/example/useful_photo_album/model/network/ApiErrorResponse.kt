package com.example.useful_photo_album.model.network

/**
 * API 호출 시 에러 응답
 *
 * @property code 에러 코드
 * @property msg 자세한 에러 설명
 * @property requiredScopes API 호출을 위해 추가로 필요한 동의 항목
 */

data class ApiErrorResponse(
    val code: Int,
    val detail: String?,
    val message: String?,
    val msg: String,
    val apiType: String? = null,
    val requiredScopes: List<String>?,
    val allowedScopes: List<String>? = null
) {
    val totalMessage = message ?: detail ?: ""
}