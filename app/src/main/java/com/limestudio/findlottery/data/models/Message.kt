package com.limestudio.findlottery.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    val id: String,
    val interlocutorId: String,
    val userId: String,
    val message: String,
    val date: Long,
    val name: String
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "interlocutorId" to interlocutorId,
            "userId" to userId,
            "message" to message,
            "date" to date,
            "name" to name
        )
    }
}