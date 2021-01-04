package com.limestudio.findlottery.presentation.ui.tricks

import androidx.lifecycle.MutableLiveData
import com.limestudio.findlottery.data.UserType
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TricksViewModel(private val trickDataSource: TicketsRepository) : BaseViewModel() {

    val tricks = MutableLiveData<List<Ticket>>()

    fun loadData(type: UserType) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val result = trickDataSource.getTickets()
            withContext(Dispatchers.Main) {
                tricks.postValue(result)
            }
        }
    }
}