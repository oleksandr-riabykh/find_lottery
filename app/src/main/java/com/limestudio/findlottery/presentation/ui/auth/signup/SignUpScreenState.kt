package com.limestudio.findlottery.presentation.ui.auth.signup

import com.limestudio.findlottery.presentation.base.BaseScreenState

sealed class SignUpScreenState : BaseScreenState() {
    object ShowProgressBar : SignUpScreenState()
    object HideProgressBar : SignUpScreenState()
    data class FilesUploaded(val urls: Map<Int, String>) : SignUpScreenState()
    object UploadError : SignUpScreenState()
    object UserSaved : SignUpScreenState()
    data class ShowMessage(val messageId: Int) : SignUpScreenState()
    data class ShowMessageString(val message: String) : SignUpScreenState()
}