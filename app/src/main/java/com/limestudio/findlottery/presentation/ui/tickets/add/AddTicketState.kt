package com.limestudio.findlottery.presentation.ui.tickets.add

import com.limestudio.findlottery.data.exceptions.ExceptionModel
import com.limestudio.findlottery.presentation.base.BaseScreenState


sealed class AddTicketState : BaseScreenState() {
    object OnSaveSuccess : AddTicketState()
    data class ShowMessageError(val exception: ExceptionModel) : AddTicketState()
    data class InputNumberValid(val isValid: Boolean) : AddTicketState()
    data class InputSetValid(val isValid: Boolean) : AddTicketState()
    data class InputGroupValid(val isValid: Boolean) : AddTicketState()
}