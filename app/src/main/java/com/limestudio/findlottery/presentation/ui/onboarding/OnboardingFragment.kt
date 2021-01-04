package com.limestudio.findlottery.presentation.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.limestudio.findlottery.R

private const val ARG_IMAGE_ID = "param1"

class OnboardingFragment : Fragment() {
    private var imageId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageId = it.getInt(ARG_IMAGE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.item_onboarding, container, false) as ImageView
        imageId?.let { inflate.setImageResource(it) }
        return inflate
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_IMAGE_ID, param1)
                }
            }
    }
}