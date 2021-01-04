package com.limestudio.findlottery.presentation.ui.auth.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.limestudio.findlottery.presentation.base.BaseViewModel

class StartViewModel : BaseViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Profile Fragment"
    }
    val text: LiveData<String> = _text
}
