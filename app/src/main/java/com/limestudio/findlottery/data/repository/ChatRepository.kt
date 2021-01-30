package com.limestudio.findlottery.data.repository

import android.content.Context
import com.limestudio.findlottery.data.firebase.FirebaseDB
import com.limestudio.findlottery.data.models.Message
import com.limestudio.findlottery.data.network.NetworkManager

class ChatRepository(val context: Context, val db: FirebaseDB) : BaseRepository() {

    private val networkManager = NetworkManager()

    fun sendMessage(message: Message) {
        db.sendMessage(message)
    }

    companion object {
        @Volatile
        private var INSTANCE: ChatRepository? = null

        fun getInstance(context: Context, db: FirebaseDB): ChatRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRepository(context, db).also { INSTANCE = it }
            }

        private fun buildRepository(context: Context, db: FirebaseDB) = ChatRepository(context, db)
    }
}