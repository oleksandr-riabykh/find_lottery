package com.limestudio.findlottery.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.limestudio.findlottery.data.exceptions.ExceptionModel
import com.limestudio.findlottery.data.exceptions.simpleExceptionHandler
import com.limestudio.findlottery.extensions.getCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel() : ViewModel() {
    val mScope = getCoroutineScope()
    val error = MutableLiveData<ExceptionModel>()
    fun gerErrorHandler() = simpleExceptionHandler { error ->
        mScope.launch {
            withContext(Dispatchers.Main) {
                this@BaseViewModel.error.postValue(error)
            }
        }
    }
}