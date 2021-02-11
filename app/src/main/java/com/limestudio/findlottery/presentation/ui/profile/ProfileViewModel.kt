package com.limestudio.findlottery.presentation.ui.profile

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.data.repository.UsersRepository
import com.limestudio.findlottery.extensions.isDayBeforeFuture
import com.limestudio.findlottery.extensions.isTicketWon
import com.limestudio.findlottery.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class ProfileViewModel(
    private val userRepository: UsersRepository, private val ticketsRepository: TicketsRepository
) : BaseViewModel() {
    val user = MutableLiveData<User>()
    val avatarUrl = MutableLiveData<File>()
    val idCardUrl = MutableLiveData<File>()
    val draws = MutableLiveData<List<Draw>>()

    fun getUser(userId: String = Firebase.auth.currentUser?.uid ?: "") {
        loadImages(userId)
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val status = async { userRepository.getUser(userId) }
            val result = status.await()
            user.postValue(result)
        }
    }

    fun deleteDraw(draw: Draw) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            ticketsRepository.deleteDraw(draw)
        }
    }

    fun loadData(userId: String?) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {

            var drawsResult =
                userId?.let { ticketsRepository.loadDraws(it) } ?: ticketsRepository.loadDraws()
            drawsResult = drawsResult.map {
                val tickets = ticketsRepository.loadTickets(it)
                it.numberWinTickets = tickets.count { ticket -> ticket.isTicketWon() }
                it.tickets = tickets
                it
            }
            drawsResult = drawsResult.filter {
                Date(it.timestamp).isDayBeforeFuture() || it.tickets.isNotEmpty()
            }

            withContext(Dispatchers.Main) {
                draws.postValue(drawsResult.sortedBy { it.timestamp })
            }
        }
    }

    private fun loadImages(userId: String) {
        userRepository.getUserAvatar(userId, { avatarUrl.postValue(it) }, {
//            error.postValue(
//                ExceptionModel(
//                    it.localizedMessage ?: it.message ?: "Unknown Error",
//                    messageId = R.string.unknown_exception
//                )
//            )
        })
        userRepository.getUserIdCard(userId, { idCardUrl.postValue(it) }, {
//            error.postValue(
//                ExceptionModel(
//                    it.localizedMessage ?: it.message ?: "Unknown Error",
//                    messageId = R.string.unknown_exception
//                )
//            )
        })
    }
}
