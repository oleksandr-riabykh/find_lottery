package com.limestudio.findlottery.extensions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun ViewModel.getCoroutineScope(): CoroutineScope {
    val mSuperVisor = SupervisorJob()
    return CoroutineScope(Dispatchers.IO + mSuperVisor)
}