package com.limestudio.findlottery.presentation.ui.game

import androidx.lifecycle.MutableLiveData
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(private val dataSource: TicketsRepository) : BaseViewModel() {

    val games = MutableLiveData<ArrayList<Ticket>>()
    val selectedGame = MutableLiveData<User>()
    val mStep = MutableLiveData<Ticket>()
    fun setSelectedStep(step: Ticket) {
        mStep.value = step
    }

    fun loadGames() {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val data = dataSource.getTickets()
            withContext(Dispatchers.Main) {
                games.postValue(data)
            }
        }
    }
}
