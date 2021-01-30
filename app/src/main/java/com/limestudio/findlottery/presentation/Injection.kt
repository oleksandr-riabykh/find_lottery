package com.limestudio.findlottery.presentation

import android.content.Context
import com.limestudio.findlottery.data.firebase.FirebaseDB
import com.limestudio.findlottery.data.repository.ChatRepository
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.data.repository.UsersRepository
import com.limestudio.findlottery.presentation.ui.ViewModelFactory

object Injection {
    private fun provideTicketsSource(context: Context) = TicketsRepository.getInstance(context)
    private fun provideChatSource(context: Context) = ChatRepository.getInstance(context, FirebaseDB())
    private fun provideUsersSource(context: Context) = UsersRepository.getInstance(context)

    fun provideViewModelFactory(context: Context) = ViewModelFactory(
        provideTicketsSource(context),
        provideUsersSource(context),
        provideChatSource(context)
    )
}