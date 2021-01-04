package com.limestudio.findlottery.data.exceptions

import com.limestudio.findlottery.R


enum class ErrorMessageCode {
    UNKNOWN,
    KOTLIN_NULL_POINTER_EXCEPTION,
    TOKEN_EXPIRED,
    SOCKET_TIMEOUT_EXCEPTION,
    ILLEGAL_STATE_EXCEPTION,
    HTTP_EXCEPTION;

    fun getMessageId(errorMessageCode: ErrorMessageCode = this): Int {
        return when (errorMessageCode) {
            UNKNOWN -> ExceptionErrors.UNKNOWN
            KOTLIN_NULL_POINTER_EXCEPTION -> ExceptionErrors.KotlinNullPointerException
            TOKEN_EXPIRED -> ExceptionErrors.UNKNOWN
            SOCKET_TIMEOUT_EXCEPTION -> ExceptionErrors.SocketTimeoutException
            ILLEGAL_STATE_EXCEPTION -> ExceptionErrors.IllegalStateException
            HTTP_EXCEPTION -> ExceptionErrors.HttpException
        }
    }
}

object ExceptionErrors {
    const val UNKNOWN = R.string.error_unknown
    const val HttpException = R.string.error_http
    const val IllegalStateException = R.string.error_unknown
    const val SocketTimeoutException = R.string.error_socket
    const val KotlinNullPointerException = R.string.error_null_pointer
}