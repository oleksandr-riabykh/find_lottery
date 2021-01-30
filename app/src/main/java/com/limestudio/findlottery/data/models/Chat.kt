package com.limestudio.findlottery.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chat(
    val userId: String,
    val messages: List<Message>,
) : Parcelable {
    fun toMap() {

    }
}