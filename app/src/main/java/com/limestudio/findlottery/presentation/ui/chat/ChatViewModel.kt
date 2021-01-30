package com.limestudio.findlottery.presentation.ui.chat

import com.limestudio.findlottery.data.models.Message
import com.limestudio.findlottery.data.repository.ChatRepository
import com.limestudio.findlottery.presentation.base.BaseViewModel
import java.util.*

class ChatViewModel(private val repository: ChatRepository) : BaseViewModel() {

    private var interlocutorId = "interlocutorId"


    fun sendMessage(messageText: String) {
        val message = Message(
            "id01",
            interlocutorId,
            "userId",
            messageText,
            Date().time,
            "interlocutorName"
        )

        repository.sendMessage(message)
    }
}