package com.limestudio.findlottery.presentation.ui.map

import androidx.lifecycle.MutableLiveData
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.models.User
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
    val filteredTickets = MutableLiveData<List<Ticket>>()
    val filteredUsers = MutableLiveData<List<User>>()
    private val allUsers = arrayListOf<User>()
    private val allTickets = arrayListOf<Ticket>()

    private var savedCity = ""

    fun filterTickets(inputNumber: String) {
        if (inputNumber.isBlank()) {
            filteredTickets.postValue(allTickets)
            filteredUsers.postValue(allUsers)
            return
        }

        val tickets = allTickets.filter { it.numbers.contains(inputNumber) }.sortedBy { it.numbers }
        filteredUsers.postValue(allUsers.filter { user -> tickets.any { it.userId == user.id } })
        filteredTickets.postValue(tickets)
    }

    fun loadAllCityTickets(city: String) {
        if (city == savedCity) return
        savedCity = city
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val result = ticketsRepository.loadTicketsByCity(city)
            val users = result.map { it.first }
            val tickets = result.map { pair -> pair.second }.flatten()
            withContext(Dispatchers.Main) {
                allUsers.clear()
                allUsers.addAll(users)
                filteredUsers.postValue(users)
            }
            withContext(Dispatchers.Main) {
                allTickets.clear()
                allTickets.addAll(tickets)
                filteredTickets.postValue(tickets)
            }
        }
    }
}