package com.limestudio.findlottery.presentation.ui.auth

import com.limestudio.findlottery.data.repository.UsersRepository
import com.limestudio.findlottery.presentation.base.BaseViewModel
import com.limestudio.findlottery.presentation.ui.custom.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

const val CODE_USER_STATUS = "status"

class AuthViewModel(private val repository: UsersRepository) : BaseViewModel() {

    val state = SingleLiveEvent<AuthState>()

    fun checkUserStatus() {
        mScope.launch (Dispatchers.IO + gerErrorHandler()){
            val status = async { repository.userStatus() }
            val result = status.await()
            if (result == null)
                state.postValue(AuthState.StartRelogin)
            else state.postValue(AuthState.StartMainActivity(result))
        }
    }
}