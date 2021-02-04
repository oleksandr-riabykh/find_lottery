package com.limestudio.findlottery.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.limestudio.findlottery.data.repository.ChatRepository
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.data.repository.UsersRepository
import com.limestudio.findlottery.presentation.ui.auth.AuthViewModel
import com.limestudio.findlottery.presentation.ui.auth.signup.SignUpViewModel
import com.limestudio.findlottery.presentation.ui.chat.ChatViewModel
import com.limestudio.findlottery.presentation.ui.map.MapsViewModel
import com.limestudio.findlottery.presentation.ui.profile.ProfileViewModel
import com.limestudio.findlottery.presentation.ui.tickets.add.AddTicketViewModel
import com.limestudio.findlottery.presentation.ui.tickets.draws.DrawsViewModel
import com.limestudio.findlottery.presentation.ui.tickets.list.TicketsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val ticketDataSource: TicketsRepository,
    private val userDataSource: UsersRepository,
    private val chatDataSource: ChatRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> ChatViewModel(
                ticketDataSource,
                chatDataSource
            ) as T
            modelClass.isAssignableFrom(AddTicketViewModel::class.java) -> AddTicketViewModel(
                ticketDataSource
            ) as T
            modelClass.isAssignableFrom(DrawsViewModel::class.java) -> DrawsViewModel(
                ticketDataSource
            ) as T
            modelClass.isAssignableFrom(TicketsViewModel::class.java) -> TicketsViewModel(
                ticketDataSource
            ) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(
                ticketDataSource
            ) as T
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(
                userDataSource
            ) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(
                userDataSource,
                ticketDataSource
            ) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}