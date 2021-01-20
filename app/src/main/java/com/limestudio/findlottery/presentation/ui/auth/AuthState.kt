package com.limestudio.findlottery.presentation.ui.auth

import com.limestudio.findlottery.presentation.base.BaseScreenState

sealed class AuthState : BaseScreenState() {
    data class OnUserStatusCheckResult(val status: Int) : AuthState()
}