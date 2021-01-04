package com.limestudio.findlottery.presentation.ui.tickets.list

import com.limestudio.findlottery.data.exceptions.ExceptionModel
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.presentation.base.BaseScreenState

sealed class TicketsState : BaseScreenState() {
    data class OnLoadCompleted(val tickets: List<Ticket>) : TicketsState()
    data class ShowInterstitial(val shouldShow: Boolean) : TicketsState()
    data class OnTicketDeleted(val ticket: Ticket) : TicketsState()
    data class OnShowMessage(val error: ExceptionModel) : TicketsState()
}