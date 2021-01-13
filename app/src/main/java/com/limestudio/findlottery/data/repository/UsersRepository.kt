package com.limestudio.findlottery.data.repository

import android.content.Context
import com.limestudio.findlottery.data.models.User
import java.util.*

class UsersRepository(val context: Context) : BaseRepository() {

    suspend fun getUserById(userId: String): ArrayList<User> {
        return arrayListOf()
    }

    suspend fun updateUserLocation() {}

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