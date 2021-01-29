package com.limestudio.findlottery.presentation.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.limestudio.findlottery.R
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.ui.ViewModelFactory

internal const val ARG_POST_ID = "postID"

class MessageDetailsFragment : Fragment() {

    private lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ChatViewModel by viewModels { viewModelFactory }
    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getString(ARG_POST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = Injection.provideViewModelFactory(requireContext())
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId?.let {
            viewModel.loadPostById(it)
        }
        viewModel.selectedPost.observe(viewLifecycleOwner, Observer {
//            titleTextView.text = it.numbers
//            descriptionTextView.text = it.numbers.platformFromHtml()
//            Picasso.get().load(it.numbers).placeholder(R.drawable.ic_profile).into(imageView)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
            return true
        }
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }
}
