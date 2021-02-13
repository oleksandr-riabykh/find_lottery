package com.limestudio.findlottery.presentation.ui.onboarding

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.limestudio.findlottery.R

class OnboardingPager(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val images = listOf(
        R.drawable.ic_buyer_onboarding_1,
        R.drawable.ic_buyer_onboarding_2,
        R.drawable.ic_buyer_onboarding_3,
        R.drawable.ic_seller_onboarding_1,
        R.drawable.ic_seller_onboarding_2,
        R.drawable.ic_seller_onboarding_3
    )

    override fun createFragment(position: Int) = OnboardingFragment.newInstance(images[position])

    override fun getItemCount() = images.size
}