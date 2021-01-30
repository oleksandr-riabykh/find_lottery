package com.limestudio.findlottery.data.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.data.models.Message

class FirebaseDB {

    private val database = Firebase.database

    private val reference = database.reference

    fun sendMessage(message: Message) {
        reference.setValue(message.toMap())
    }

}