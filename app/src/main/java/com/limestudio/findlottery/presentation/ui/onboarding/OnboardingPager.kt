package com.limestudio.findlottery.presentation.ui.onboarding

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.limestudio.findlottery.R

class OnboardingPager(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val images = listOf(
        R.drawable.ic_star_selected,
        R.drawable.ic_star_selected,
        R.drawable.ic_star_selected,
        R.drawable.ic_star_selected,
        R.drawable.ic_star_selected
    )

    override fun createFragment(position: Int) = OnboardingFragment.newInstance(images[position])

    override fun getItemCount() = images.size
}