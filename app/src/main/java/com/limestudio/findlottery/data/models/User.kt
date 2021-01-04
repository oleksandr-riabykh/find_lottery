package com.limestudio.findlottery.data.models


data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val phoneNumber: String?,
    val city: String,
    val national_id: String?
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "lastName" to lastName,
            "phoneNumber" to phoneNumber,
            "city" to city,
            "nationalId" to national_id
        )
    }
}