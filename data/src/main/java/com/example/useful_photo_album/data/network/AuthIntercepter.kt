package com.example.useful_photo_album.data.network

import com.example.useful_photo_album.data.common.Constants
import com.example.useful_photo_album.pref.PreferenceStorage
import com.example.useful_photo_album.shared.model.network.ApiErrorCause
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val usedAccessToken =
            runBlocking {
                preferenceStorage.accessToken.first()
            }

        val request = chain.request().withAccessToken(usedAccessToken)

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
        .addHeader(Constants.AUTHORIZATION, "${Constants.CLIENT_ID} $accessToken")
        .build()

