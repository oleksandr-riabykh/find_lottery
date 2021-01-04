package com.limestudio.findlottery.data.repository

import android.content.Context
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.network.NetworkManager
import java.util.*

class ChatRepository(val context: Context) : BaseRepository() {

    private val networkManager = NetworkManager()

    suspend fun getPostById(id: String): Ticket? {
        return null
    }

    suspend fun getTickets(): ArrayList<Ticket> {
        val uiFeed = arrayListOf<Ticket>()
        return if (uiFeed.isNotEmpty()) uiFeed else getTicketNetwork()
    }

    private suspend fun getTicketNetwork() = networkManager.webservice.getFeed()

    fun saveTicket(ticket: Ticket) {
        TODO("Not yet implemented")
    }

    fun deleteTicket(ticket: Ticket) {

    }

    fun loadTickets(ticketData: Long) = listOf<Ticket>()


    fun loadDraws(): List<Draw> = listOf()

    fun createDraw(date: Date): Draw {
        TODO("Not yet implemented")
    }

    fun deleteDraw(draw: Any) {
        TODO("Not yet implemented")
    }

    companion object {
        @Volatile
        private var INSTANCE: ChatRepository? = null

        fun getInstance(context: Context): ChatRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRepository(context).also { INSTANCE = it }
            }

        private fun buildRepository(context: Context) = ChatRepository(context)
    }
}