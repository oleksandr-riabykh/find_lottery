package com.limestudio.findlottery.presentation.ui.map

import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.presentation.base.BaseViewModel
import com.limestudio.findlottery.presentation.ui.custom.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel(
    private val ticketsRepository: TicketsRepository
) : BaseViewModel() {
    val state = SingleLiveEvent<MapState>()
    fun searchTickets(numbers: String, city: String) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val result = ticketsRepository.loadTicketsByCity(city)
            val users = result.map { it.first }
            val tickets = result.map { pair -> pair.second }.flatten()
            withContext(Dispatchers.Main) {
                state.postValue(MapState.OnUsersLoaded(users))
            }
            withContext(Dispatchers.Main) {
                state.postValue(MapState.OnTicketsLoaded(tickets))
            }
        }
    }
}