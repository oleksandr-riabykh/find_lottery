package com.limestudio.findlottery.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Ticket(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val drawId: String = "",
    val timestamp: Long = 0L,
    val date: String = "",
    val numbers: String = "",
    val set: String = "",
    val progress: String = "",
    var status: List<Int>? = listOf()
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "drawId" to drawId,
            "timestamp" to timestamp,
            "date" to date,
            "numbers" to numbers,
            "set" to set,
            "progress" to progress
        )
    }

    fun fromMap(id: String, map: Map<String, Any?>): Ticket {
        return Ticket(
            id = id,
            userId = map["userId"].toString(),
            drawId = map["drawId"].toString(),
            timestamp = map["timestamp"]?.toString()?.toLong() ?: System.currentTimeMillis(),
            numbers = map["numbers"].toString(),
            date = map["date"].toString(),
            set = map["set"].toString(),
            progress = map["progress"].toString(),
        )
    }
}