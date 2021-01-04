package com.limestudio.findlottery.data.exceptions

import com.limestudio.findlottery.R
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import java.net.SocketTimeoutException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

@Suppress("FunctionName")
inline fun simpleExceptionHandler(crossinline handler: (error: ExceptionModel) -> Unit): CoroutineExceptionHandler =
    object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

        override fun handleException(context: CoroutineContext, exception: Throwable) =
            handler.invoke(processError(exception))
    }

fun processError(exception: Throwable?): ExceptionModel {
    return when (exception) {
        is HttpException -> ExceptionModel(
            exception.message(),
            ErrorMessageCode.HTTP_EXCEPTION,
            messageId = R.string.insert_validation_fail
        )
        is IllegalStateException -> ExceptionModel(
            exception.message(),
            ErrorMessageCode.ILLEGAL_STATE_EXCEPTION,
            messageId = R.string.insert_validation_fail
        )
        is SocketTimeoutException -> ExceptionModel(
            exception.message(),
            ErrorMessageCode.SOCKET_TIMEOUT_EXCEPTION,
            messageId = R.string.insert_validation_fail
        )
        is KotlinNullPointerException -> ExceptionModel(
            exception.message(),
            ErrorMessageCode.KOTLIN_NULL_POINTER_EXCEPTION,
            messageId = R.string.insert_validation_fail
        )
        is TokenExpired -> {
            ExceptionModel(
                exception.message(),
                ErrorMessageCode.TOKEN_EXPIRED,
                true,
                R.string.insert_validation_fail
            )
        }
        else -> ExceptionModel(
            exception.message(),
            ErrorMessageCode.UNKNOWN,
            messageId = R.string.insert_validation_fail
        )
    }
}

fun Throwable?.message(): String {
    return this?.localizedMessage ?: this?.message.ifNullShowUnknownError()
}

fun String?.ifNullShowErrorMessage(): String {
    return this ?: "Success but a message from server null"
}

fun String?.ifNullShowUnknownError(): String {
    return this ?: "Unknown error"
}