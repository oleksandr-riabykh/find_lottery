package com.limestudio.findlottery.presentation.ui.game.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.ui.ViewModelFactory
import com.limestudio.findlottery.presentation.ui.game.GameViewModel

class CategoryItemFragment : Fragment(), OnCategoryInteractionListener {

    private lateinit var viewAdapter: CategoryAdapter
    private lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: GameViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewAdapter = CategoryAdapter(this@CategoryItemFragment)
        viewModelFactory = Injection.provideViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        if (view is RecyclerView) {
            with(view) {
                adapter = viewAdapter
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadGames()
        viewModel.games.observe(viewLifecycleOwner, Observer {
//            viewAdapter.setData(it)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            showWarning(it.message)
        })
    }

    override fun onCategoryClicked(item: User) {
        navigateTo(
            R.id.navigation_game_details,
            R.id.navigation_tickets,
            false,
            bundle = Bundle().apply {
                putString("ARG_GAME_ID", item.phoneNumber)
            })
    }
}
