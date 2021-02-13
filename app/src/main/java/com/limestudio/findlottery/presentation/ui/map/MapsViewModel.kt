package com.limestudio.findlottery.presentation.ui.map

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
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

    fun filterTickets(inputNumber: String) {
        if (inputNumber.isBlank() || inputNumber.length < 2) {
            filteredTickets.postValue(listOf())
            filteredUsers.postValue(allUsers)
            return
        }

        val tickets = allTickets.filter { it.numbers.contains(inputNumber) }.sortedBy { it.numbers }
        filteredUsers.postValue(allUsers.filter { user -> tickets.any { it.userId == user.id && it.userId != FirebaseAuth.getInstance().currentUser?.uid } })
        filteredTickets.postValue(tickets)
    }

    fun loadAllCityTickets(city: String) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val result = ticketsRepository.loadTicketsByCity(city)
            val users = result.map { it.first }
            val tickets = result.map { pair -> pair.second }.flatten()
            withContext(Dispatchers.Main) {
                allUsers.clear()
                allUsers.addAll(users.filter { it.id != FirebaseAuth.getInstance().currentUser?.uid })
                filteredUsers.postValue(users.filter { it.id != FirebaseAuth.getInstance().currentUser?.uid })
            }
            withContext(Dispatchers.Main) {
                allTickets.clear()
                allTickets.addAll(tickets.filter { it.userId != FirebaseAuth.getInstance().currentUser?.uid })
            }
        }
    }

    fun ticketSelected(ticket: Ticket) {
        val filter = allUsers.filter { user -> ticket.userId == user.id }
        filteredUsers.postValue(filter)
        state.postValue(MapState.OnTicketSelected(ticket, filter.first()))
    }

    fun setDefaultLocation(defaultLocation: LatLng) =
        ticketsRepository.setDefaultLocation(defaultLocation)
}