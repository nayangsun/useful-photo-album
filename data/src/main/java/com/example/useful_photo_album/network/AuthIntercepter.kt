package com.example.useful_photo_album.network

import com.example.useful_photo_album.common.Constants
import com.example.useful_photo_album.model.network.ApiErrorCause
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        request = request.newBuilder()
            .addHeader(Constants.AUTHORIZATION, "Bearer ")
            .build()
        return  chain.proceedApiError(request) { response, error ->
            Timber.e("proceedApiError 콜백 ${error.reason}")
            if (error.reason == ApiErrorCause.InvalidToken) {
                Timber.e("InvalidToken")
            }
            response
        }
    }
}
