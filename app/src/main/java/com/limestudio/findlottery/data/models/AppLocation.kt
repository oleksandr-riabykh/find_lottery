package com.limestudio.findlottery.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppLocation(
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )
    }
}