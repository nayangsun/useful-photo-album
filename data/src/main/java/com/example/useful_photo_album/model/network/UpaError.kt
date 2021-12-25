package com.example.useful_photo_album.model.network

import com.example.useful_photo_album.json.UnknownValue
import com.google.gson.annotations.SerializedName



@Suppress("Unused")
sealed class UpaError(open val msg: String) : RuntimeException(msg) {
}

/**
 * API 에러
 */
data class ApiError(
    val statusCode: Int,
    val reason: ApiErrorCause,
    val response: ApiErrorResponse
) : UpaError(response.message)
/**
 * 로그인 에러
 */

data class AuthError(
    val statusCode: Int,
    val reason: AuthErrorCause,
    val response: AuthErrorResponse
) : UpaError(response.errorDescription ?: response.error)

/**
 * SDK 내에서 발생하는 클라이언트 에러
 */

data class ClientError(
    val reason: ClientErrorCause,
    override val msg: String = reason.javaClass.getField(reason.name).getAnnotation(Description::class.java)?.value
        ?: "Client-side error"
) : UpaError(msg)

/**
 * [AuthError]의 발생 원인
 */
enum class AuthErrorCause {
    /** 요청 파라미터 오류 */
    @SerializedName("invalid_request")
    InvalidRequest,

    /** 유효하지 않은 앱 */
    @SerializedName("invalid_client")
    InvalidClient,

    /** 유효하지 않은 scope ID */
    @SerializedName("invalid_scope")
    InvalidScope,

    /** 인증 수단이 유효하지 않아 인증할 수 없는 상태 */
    @SerializedName("invalid_grant")
    InvalidGrant,

    /** 설정이 올바르지 않음 (android key hash) */
    @SerializedName("misconfigured")
    Misconfigured,

    /** 앱이 요청 권한이 없음 */
    @SerializedName("unauthorized")
    Unauthorized,

    /** 접근이 거부 됨 (동의 취소) */
    @SerializedName("access_denied")
    AccessDenied,

    /** 서버 내부 에러 */
    @SerializedName("server_error")
    ServerError,

    /** 기타 에러 */
    @UnknownValue
    Unknown,
}

/**
 * [ApiError]의 발생 원인
 */
enum class ApiErrorCause(val errorCode: Int) {


    /** The request was unacceptable, often due to missing a required parameter */
    @SerializedName("-400")
    BadRequest(-400),

    /** Invalid Access Token */
    @SerializedName("-401")
    Unauthorized(-401),

    /** Missing permissions to perform request */
    @SerializedName("-403")
    Forbidden(-403),

    /** The requested resource doesn’t exist */
    @SerializedName("-404")
    NotFound(-404),

    /** Something went wrong on our end */
    @SerializedName("-500")
    ServerError(-500),

    /** Something went wrong on our end */
    @SerializedName("-503")
    ServerErrorAnother(-503),


    /** 기타 에러 */
    @UnknownValue
    Unknown(Int.MAX_VALUE);
}

/**
 * [ClientError]의 발생 원인
 */
enum class ClientErrorCause {

    /** 기타 에러 */
    @Description("unknown error.")
    Unknown,

    /** 요청 취소 */
    @Description("user cancelled.")
    Cancelled,

    /** API 요청에 사용할 토큰이 없음 */
    @Description("authentication tokens don't exist.")
    TokenNotFound,

    /** 지원되지 않는 기능 */
    @Description("user is unauthenticated.")
    NotSupported,

    /** 잘못된 파라미터 */
    @Description("bad parameters.")
    BadParameter,

    /** 정상적으로 실행할 수 없는 상태 */
    @Description("illegal state.")
    IllegalState
}


/**
 * @suppress
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Description (
    val value : String
)