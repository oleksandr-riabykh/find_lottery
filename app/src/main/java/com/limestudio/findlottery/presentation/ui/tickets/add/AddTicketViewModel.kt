package com.limestudio.findlottery.presentation.ui.tickets.add

import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.exceptions.ExceptionModel
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.extensions.containsNumber
import com.limestudio.findlottery.presentation.base.BaseViewModel
import com.limestudio.findlottery.presentation.ui.custom.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTicketViewModel(private val ticketsRepository: TicketsRepository) : BaseViewModel() {
    val state = SingleLiveEvent<AddTicketState>()
    fun saveTicket(ticket: Ticket) {

        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            if (isTicketValid(ticket)) {
                ticketsRepository.saveTicket(ticket)
                withContext(Dispatchers.Main) {
                    state.postValue(AddTicketState.OnSaveSuccess)
                }
            } else {
                withContext(Dispatchers.Main) {
                    state.postValue(
                        AddTicketState.ShowMessageError(
                            ExceptionModel(
                                "Please, make sure you insert correct data",
                                messageId = R.string.insert_validation_fail
                            )
                        )
                    )
                }
            }
        }
    }

    private fun isTicketValid(ticket: Ticket) = ticket.numbers.isNotBlank() &&
            validateTicketNumber(ticket.numbers) &&
            validateGroup(ticket.progress) &&
            validateSet(ticket.set)

    fun validateSet(s: String?) = if (s != null && s.length == 2 && s.toString().containsNumber()) {
        state.postValue(AddTicketState.InputSetValid(true))
        true
    } else {
        state.postValue(AddTicketState.InputSetValid(false))
        false
    }

    fun validateTicketNumber(s: String?) =
        if (s != null && s.length == 6 && s.toString().containsNumber()) {
            state.postValue(AddTicketState.InputNumberValid(true))
            true
        } else {
            state.postValue(AddTicketState.InputNumberValid(false))
            false
        }

    fun validateGroup(s: String?) =
        if (s != null && s.length == 2 && s.toString().containsNumber()) {
            state.postValue(AddTicketState.InputGroupValid(true))
            true
        } else {
            state.postValue(AddTicketState.InputGroupValid(false))
            false
        }
}