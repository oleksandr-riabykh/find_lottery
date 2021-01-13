package com.limestudio.findlottery.presentation.ui.profile.seller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.limestudio.findlottery.presentation.base.BaseViewModel

class SellerProfileViewModel : BaseViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Profile Fragment"
    }
    val text: LiveData<String> = _text
}
