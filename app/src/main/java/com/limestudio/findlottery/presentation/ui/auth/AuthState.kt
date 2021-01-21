package com.limestudio.findlottery.presentation.ui.auth

import com.limestudio.findlottery.presentation.base.BaseScreenState

sealed class AuthState : BaseScreenState() {
    object StartRelogin : AuthState()
    data class StartMainActivity(val status: Int?) : AuthState()
}