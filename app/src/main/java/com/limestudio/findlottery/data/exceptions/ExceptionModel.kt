package com.limestudio.findlottery.data.exceptions

data class ExceptionModel(
    val message: String,
    val errorMessageCode: ErrorMessageCode = ErrorMessageCode.UNKNOWN,
    val emergencyBreakSession: Boolean = false,
    val messageId: Int
)
