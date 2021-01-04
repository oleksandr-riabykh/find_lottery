package com.limestudio.findlottery.presentation.ui.tricks


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TricksTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val mFragmentList: MutableList<Fragment> =
        ArrayList()
    private val mFragmentTitleList: MutableList<String> =
        ArrayList()

    override fun getItemCount(): Int = mFragmentList.size

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun getTitle(position: Int) = mFragmentTitleList[position]

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}