package com.limestudio.findlottery.presentation.ui.tricks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.UserType
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.ui.ViewModelFactory
import kotlinx.android.synthetic.main.recycler.recycler_view as recyclerView

class TricksListFragment : Fragment(), OnListTrickInteractionListener {

    companion object {

        const val ARG_TRICK_TYPE = "trick-type"

        @JvmStatic
        fun newInstance(columnCount: UserType) =
            TricksListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TRICK_TYPE, columnCount.name)
                }
            }
    }

    private lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: TricksViewModel by viewModels { viewModelFactory }
    private lateinit var viewAdapter: TricksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = Injection.provideViewModelFactory(requireContext())
        viewAdapter = TricksAdapter(this@TricksListFragment)
        viewModel.tricks.observe(viewLifecycleOwner, Observer {
            viewAdapter.setData(it)

        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            showWarning(it.message)
        })
        return inflater.inflate(R.layout.recycler, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val type = if (arguments?.getString(ARG_TRICK_TYPE)
                ?.equals(UserType.GUEST.name) == true
        ) UserType.GUEST else UserType.SELLER
        viewModel.loadData(type)
        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                adapter = viewAdapter
            }
        }
    }

    override fun onListFragmentInteraction(item: Ticket?) {
//        Toast.makeText(requireContext(), item?.title, Toast.LENGTH_SHORT).show()
    }
}
