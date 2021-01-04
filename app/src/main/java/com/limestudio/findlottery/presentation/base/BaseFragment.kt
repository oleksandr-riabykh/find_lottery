package com.limestudio.findlottery.presentation.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.ui.ViewModelFactory

open class BaseFragment : Fragment() {
    protected lateinit var viewModelFactory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = Injection.provideViewModelFactory(requireContext())
    }
}