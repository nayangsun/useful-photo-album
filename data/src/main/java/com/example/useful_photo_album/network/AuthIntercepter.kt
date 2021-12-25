package com.example.useful_photo_album.network

import com.example.useful_photo_album.common.Constants
import com.example.useful_photo_album.model.network.ApiErrorCause
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber


class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val usedAccessToken = null
        val request =
            usedAccessToken?.let {
                chain.request().withAccessToken(it)
            } ?: chain.request()

        return chain.proceedApiError(request) { response, error ->

            // 401(-401) 발생 시
            if (error.reason == ApiErrorCause.Unauthorized) {
                Timber.e("InvalidToken")
            }
            response
        }
    }
}


inline fun Request.withAccessToken(accessToken: String) =
    newBuilder()
        .removeHeader(Constants.AUTHORIZATION)
        .addHeader(Constants.AUTHORIZATION, "${Constants.BEARER} $accessToken")
        .build()
