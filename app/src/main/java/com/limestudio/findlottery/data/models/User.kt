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
            "type" to type
        )
    }
}

//lastName=tes, phoneNumber=, nationalId=, city=bangkok, name=Oleksandr, location={latitude=13.4565, longitude=100.45655}, id=NCh7sgfnp7VeffVTx8oNAvfhXdc2