package com.limestudio.findlottery.presentation.ui.profile.edit

import com.limestudio.findlottery.presentation.base.BaseScreenState

sealed class EditProfileScreenState : BaseScreenState() {
    object ShowProgressBar : EditProfileScreenState()
    object HideProgressBar : EditProfileScreenState()
    data class FilesUploaded(val urls: Map<Int, String>) : EditProfileScreenState()
    object UploadError : EditProfileScreenState()
    object UserSaved : EditProfileScreenState()
    data class ShowMessage(val messageId: Int) : EditProfileScreenState()
    data class ShowMessageString(val message: String) : EditProfileScreenState()
}