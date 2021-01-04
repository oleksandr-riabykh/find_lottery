package com.limestudio.findlottery.presentation.ui.chat

import androidx.lifecycle.MutableLiveData
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.repository.ChatRepository
import com.limestudio.findlottery.data.repository.TicketsRepository
import com.limestudio.findlottery.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val ticketDataSource: TicketsRepository,
    private val chatDataSource: ChatRepository
) : BaseViewModel() {
    val posts = MutableLiveData<ArrayList<Ticket>>()
    val selectedPost = MutableLiveData<Ticket>()
    fun loadPostById(id: String) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val post = chatDataSource.getPostById(id)
            withContext(Dispatchers.Main) {
                selectedPost.postValue(post)
            }
        }
    }

    fun loadPosts() {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val data = ticketDataSource.getTickets()
            withContext(Dispatchers.Main) {
                posts.postValue(data)
            }
        }
    }
}