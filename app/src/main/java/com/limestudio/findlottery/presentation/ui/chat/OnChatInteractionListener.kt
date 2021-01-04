package com.limestudio.findlottery.presentation.ui.chat

import com.limestudio.findlottery.data.models.Ticket

interface OnChatInteractionListener {
    fun onPostClicked(item: Ticket)
}