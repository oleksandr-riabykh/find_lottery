package com.limestudio.findlottery.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Draw(
    val id: String = "",
    val userId: String? = "",
    val timestamp: Long = 0L,
    val rawData: String = "",
    var numberWinTickets: Int = 0,
    var tickets: List<Ticket> = listOf(),
    var status: Int = 0
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "rawData" to rawData,
            "timestamp" to timestamp
        )
    }

    fun fromMap(id: String, map: Map<String, Any?>): Draw {
        return Draw(
            id = id,
            userId = map["userId"].toString(),
            rawData = map["rawData"].toString(),
            timestamp = map["timestamp"]?.toString()?.toLong() ?: System.currentTimeMillis()
        )
    }
}