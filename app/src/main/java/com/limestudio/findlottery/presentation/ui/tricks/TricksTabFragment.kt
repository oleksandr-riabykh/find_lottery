package com.limestudio.findlottery.presentation.ui.tricks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.UserType
import kotlinx.android.synthetic.main.fragment_tricks.pager as viewPager
import kotlinx.android.synthetic.main.fragment_tricks.tab_layout as tabLayout

class TricksTabFragment : Fragment() {
    private lateinit var tabAdapter: TricksTabAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tricks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabAdapter = TricksTabAdapter(this)
        tabAdapter.addFragment(TricksListFragment.newInstance(UserType.SELLER), "Recommended")
        tabAdapter.addFragment(TricksListFragment.newInstance(UserType.GUEST), "Forbidden")
        viewPager.adapter = tabAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabAdapter.getTitle(position)
        }.attach()
    }
}
