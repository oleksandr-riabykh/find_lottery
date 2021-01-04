package com.limestudio.findlottery.presentation.ui.tickets.list

import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.extensions.isTicketWon
import com.limestudio.findlottery.presentation.base.BaseViewModel
import com.limestudio.findlottery.presentation.ui.custom.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val NUMBER_OF_TICKETS_TO_SHOW_ADS = 5

class TicketsViewModel(private val ticketsRepository: TicketsRepository) : BaseViewModel() {
    val state = SingleLiveEvent<TicketsState>()
    fun loadData(draw: Draw) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val loadTickets = ticketsRepository.loadTickets(draw)
            withContext(Dispatchers.Main) {
                state.postValue(TicketsState.OnLoadCompleted(loadTickets.sortedBy { ticket -> !ticket.isTicketWon() }))
            }
        }
    }

    fun deleteTicket(ticket: Ticket) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            ticketsRepository.deleteTicket(ticket)
            withContext(Dispatchers.Main) {
                state.postValue(TicketsState.OnTicketDeleted(ticket))
            }
        }
    }

    fun checkInterstitial() {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val tickets = ticketsRepository.getTicketCount()
            withContext(Dispatchers.Main) {
                state.postValue(TicketsState.ShowInterstitial((tickets % NUMBER_OF_TICKETS_TO_SHOW_ADS == 0) && (tickets != 0)))
            }
        }
    }
}