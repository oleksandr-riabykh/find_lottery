package com.limestudio.findlottery.presentation.ui.tickets.draws

import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.extensions.isDayBeforeFuture
import com.limestudio.findlottery.extensions.isTicketWon
import com.limestudio.findlottery.presentation.base.BaseViewModel
import com.limestudio.findlottery.presentation.ui.custom.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DrawsViewModel(private val ticketsRepository: TicketsRepository) : BaseViewModel() {
    val state = SingleLiveEvent<DrawsState>()
    fun loadData() {
//        state.postValue(DrawsState.ShowProgress(true)) //todo: use view model for control loader indicator
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
//            val hasWinTicket = ticketsRepository.syncDraws()
//            withContext(Dispatchers.Main) {
//                if (hasWinTicket) state.postValue(DrawsState.OnWinCombinationsReceived("New win draw has arrived"))
//            }

            var draws = ticketsRepository.loadDraws()
            draws = draws.map {
                val tickets = ticketsRepository.loadTickets(it)
                it.numberWinTickets = tickets.count { ticket -> ticket.isTicketWon() }
                it.tickets = tickets
                it
            }
            draws = draws.filter {
                Date(it.timestamp).isDayBeforeFuture() || it.tickets.isNotEmpty()
            }

//            draws = draws.filter {
//                it.numberWinTickets != 0
//            }

            withContext(Dispatchers.Main) {
                state.postValue(DrawsState.OnLoadCompleted(draws.sortedBy { it.timestamp }))
            }
            if (draws.isEmpty())
                withContext(Dispatchers.Main) {
                    state.postValue(DrawsState.ShowProgress(false))
                }
        }
    }

    fun createFutureDraw(date: Date) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            ticketsRepository.createDraw(date) {
                state.postValue(DrawsState.OnDrawCreated(it))
            }
        }
    }

    fun deleteDraw(draw: Draw) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            ticketsRepository.deleteDraw(draw)
            withContext(Dispatchers.Main) {
                state.postValue(DrawsState.OnDrawDeleted(draw))
            }
        }
    }
}