package com.limestudio.findlottery.presentation.ui.chat.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.ui.ViewModelFactory
import com.limestudio.findlottery.presentation.ui.chat.ChatViewModel
import com.limestudio.findlottery.presentation.ui.chat.OnChatInteractionListener
import kotlinx.android.synthetic.main.recycler.recycler_view as recyclerView

internal const val ARG_POST_ID = "postID"

class ChatListFragment : Fragment(), OnChatInteractionListener {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var chatListAdapter: ChatListAdapter
    private val viewModel: ChatViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = Injection.provideViewModelFactory(requireContext())
        chatListAdapter = ChatListAdapter(this)
//        viewModel.posts.observe(viewLifecycleOwner, Observer {
//            chatListAdapter.addData(it)
//
//        })
        viewModel.error.observe(viewLifecycleOwner, Observer { showWarning(it.message) })
        return inflater.inflate(R.layout.recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            adapter = chatListAdapter
        }
//        viewModel.loadPosts()
    }

    override fun onPostClicked(item: Ticket) {
        navigateTo(
            R.id.navigation_chat,
            R.id.navigation_map,
            false,
            bundle = Bundle().apply {
                putString(ARG_POST_ID, item.numbers)
            })
    }
}
