package com.limestudio.findlottery.presentation.ui.map

import com.limestudio.findlottery.data.exceptions.ExceptionModel
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.presentation.base.BaseScreenState


sealed class MapState : BaseScreenState() {
    data class OnTicketSelected(val ticket: Ticket, val user: User) : MapState()
    data class OnShowMessage(val error: ExceptionModel) : MapState()
}