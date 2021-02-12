package com.limestudio.findlottery.presentation.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.limestudio.findlottery.R
import com.limestudio.findlottery.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.item_onboarding.*

class OnboardingFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.item_onboarding, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            image.setImageResource(it.getInt(ARG_IMAGE_ID))
        }
    }

    companion object {
        private const val ARG_IMAGE_ID = "param1"

        @JvmStatic
        fun newInstance(param1: Int) =
            OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_IMAGE_ID, param1)
                }
            }
    }
}