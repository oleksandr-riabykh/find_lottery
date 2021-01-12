package com.limestudio.findlottery.presentation.ui.map

import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.data.repository.UsersRepository
import com.limestudio.findlottery.presentation.base.BaseViewModel
import com.limestudio.findlottery.presentation.ui.custom.SingleLiveEvent

class MapsViewModel(
    private val ticketsRepository: TicketsRepository,
    private val usersRepository: UsersRepository
) : BaseViewModel() {
    val state = SingleLiveEvent<MapState>()
    fun searchTickets() {
    }
}