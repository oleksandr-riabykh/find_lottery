package com.limestudio.findlottery.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.limestudio.findlottery.data.firebase.FirebaseManager
import com.limestudio.findlottery.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UsersRepository(val context: Context) : BaseRepository() {

    suspend fun getUserById(userId: String): ArrayList<User> {
        return arrayListOf()
    }

    suspend fun userStatus(): Int? {
        return withContext(mScope.coroutineContext + Dispatchers.IO) {
            return@withContext FirebaseAuth.getInstance().currentUser?.uid?.let {
                FirebaseManager(null).userStatus(it)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UsersRepository? = null

        fun getInstance(context: Context): UsersRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRepository(context).also { INSTANCE = it }
            }

        private fun buildRepository(context: Context) = UsersRepository(context)
    }
}