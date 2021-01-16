package com.limestudio.findlottery.data.repository

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.data.firebase.FirebaseManager
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.data.network.NetworkManager
import com.limestudio.findlottery.extensions.getWinCombinationList
import com.limestudio.findlottery.extensions.toDateFormat
import java.util.*

class TicketsRepository(val context: Context) : BaseRepository() {

    private val networkManager = NetworkManager()

    suspend fun getTickets(): ArrayList<Ticket> {
        val uiFeed = arrayListOf<Ticket>()
        return if (uiFeed.isNotEmpty()) uiFeed else getTicketNetwork()
    }

    private suspend fun getTicketNetwork() = networkManager.webservice.getFeed()

    fun saveTicket(ticket: Ticket): Ticket {
        FirebaseManager(null).addTicket(ticket)
        return ticket
    }

    suspend fun deleteTicket(ticket: Ticket) = FirebaseManager(null).deleteTicket(ticket)

    suspend fun loadTickets(draw: Draw): List<Ticket> {
        val tickets = FirebaseManager(null).getTickets(draw.id)

        return tickets.map {
            it.status = draw.getWinCombinationList(it)
            it
        }
    }

    suspend fun loadTicketsByCity(city: String): List<Pair<User, List<Ticket>>> {
        val result = arrayListOf<Pair<User, List<Ticket>>>()
//        val users = FirebaseManager(null).getUsersByCity(city)
        val users = FirebaseManager(null).getUsers()
        users.forEach { user ->
            val tickets = FirebaseManager(null).getTicketsByUserId(user.id)
            result.add(Pair(user, tickets))
        }
        return result
    }


    suspend fun getTicketCount() =
        FirebaseManager(null).getUserTicketsCount()

    suspend fun loadDraws() =
        Firebase.auth.currentUser?.uid?.let { FirebaseManager(null).getDraws(it) }
            ?: mutableListOf()

    fun createDraw(date: Date, onSuccess: (draws: Draw) -> Unit) =
        FirebaseManager(null).getDrawsByDate(date.toDateFormat()).addOnSuccessListener { snapshot ->
            if (snapshot.documents.isNotEmpty()) {
                snapshot.documents[0].data?.let {
                    onSuccess(
                        Draw().fromMap(
                            snapshot.documents[0].id,
                            it
                        )
                    )
                }
            } else {
                val draw = Draw(
                    id = UUID.randomUUID().toString(),
                    userId = Firebase.auth.currentUser?.uid,
                    date = date.toDateFormat(),
                    timestamp = date.time
                )
                FirebaseManager(null).addDraw(draw)
                onSuccess(draw)
            }

        }

    suspend fun deleteDraw(draw: Draw) = FirebaseManager(null).deleteDraw(draw)

    companion object {
        @Volatile
        private var INSTANCE: TicketsRepository? = null

        fun getInstance(context: Context): TicketsRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRepository(context).also { INSTANCE = it }
            }

        private fun buildRepository(context: Context) = TicketsRepository(context)
    }
}