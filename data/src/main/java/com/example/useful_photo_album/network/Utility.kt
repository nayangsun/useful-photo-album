package com.example.useful_photo_album.network

import com.example.useful_photo_album.common.UpaJson
import com.example.useful_photo_album.model.network.ApiError
import com.example.useful_photo_album.model.network.ApiErrorCause
import com.example.useful_photo_album.model.network.ApiErrorResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody


inline fun Interceptor.Chain.proceedBodyString(
    request: Request,
    bodyHandler: (response: Response, bodyString: String?) -> Response
): Response {
    val originalResponse = proceed(request)
    val body = originalResponse.body
    val bodyString = body?.string()

    // 에러 응답 파싱을 위해 body를 한번 소비했기 때문에 새로 만들어주지 않으면 다른 인터셉터에서 body 접근 시 크래시 난다.
    val newResponse =
        originalResponse.newBuilder()
            .body(ResponseBody.create(body?.contentType(), bodyString!!))
            .build()
    return bodyHandler(newResponse, bodyString)
}

/**
 * @suppress
 */
inline fun Interceptor.Chain.proceedApiError(
    request: Request,
    errorHandler: (response: Response, error: ApiError) -> Response
): Response =
    proceedBodyString(request) { response, bodyString ->
        if (!response.isSuccessful) {

            /**
             *  HTTP 상태 코드까지 파싱해서 가져올수 있도록 만들어 두었다.
             */
            val apiErrorResponse = bodyString?.let {
                UpaJson.fromJson<ApiErrorResponse>(it, ApiErrorResponse::class.java)
            }
            val apiErrorCause = apiErrorResponse?.let {
                UpaJson.fromJson<ApiErrorCause>(it.code.toString(), ApiErrorCause::class.java)
            }

            if (apiErrorCause != null && apiErrorResponse != null) {
                return errorHandler(response, ApiError(response.code, apiErrorCause, apiErrorResponse))
            }

            if (response.code == 401 && apiErrorResponse != null) {
                val apiError = apiErrorResponse.let {
                    UpaJson.fromJson<ApiErrorCause>("-401", ApiErrorCause::class.java)
                }
                return errorHandler(response, ApiError(response.code, apiError, apiErrorResponse))
            }

            /**
             *  403은 사용안하도록 계획
             */
            if (response.code == 403 && apiErrorResponse != null ) {
                val apiError = apiErrorResponse.let {
                    UpaJson.fromJson<ApiErrorCause>("-401", ApiErrorCause::class.java)
                }
                return errorHandler(response, ApiError(response.code, apiError, apiErrorResponse))
            }
        }
        return response
    }
