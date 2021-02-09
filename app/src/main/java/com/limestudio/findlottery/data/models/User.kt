package com.limestudio.findlottery.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String? = "",
    val lastName: String? = "",
    val phoneNumber: String? = "",
    val city: String? = "",
    val location: AppLocation? = null,
    val nationalId: String? = "",
    var avatar: String? = "",
    var photoId: String? = "",
    var whatsapp: String? = "",
    var wechat: String? = "",
    var line: String? = "",
    val type: Int? = 0
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "lastName" to lastName,
            "phoneNumber" to phoneNumber,
            "location" to location,
            "city" to city,
            "nationalId" to nationalId,
            "whatsapp" to whatsapp,
            "wechat" to wechat,
            "line" to line,
            "type" to type
        )
    }
}