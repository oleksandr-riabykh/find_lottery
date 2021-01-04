package com.limestudio.findlottery.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class BaseRepository {
    val mSuperVisor = SupervisorJob()
    val mScope = CoroutineScope(Dispatchers.IO + mSuperVisor)
}