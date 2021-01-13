package com.limestudio.findlottery.presentation.ui.map

import com.limestudio.findlottery.data.exceptions.ExceptionModel
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.presentation.base.BaseScreenState


sealed class MapState : BaseScreenState() {
    data class OnTicketsLoaded(val tickets: List<Ticket>) : MapState()
    data class OnUsersLoaded(val users: List<User>) : MapState()
    data class ShowProgress(val shouldShow: Boolean) : MapState()
    data class OnShowMessage(val error: ExceptionModel) : MapState()
}