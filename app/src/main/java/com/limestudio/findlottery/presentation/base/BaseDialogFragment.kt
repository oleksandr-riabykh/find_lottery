package com.limestudio.findlottery.presentation.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.limestudio.findlottery.R
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.ui.ViewModelFactory


open class BaseDialogFragment : DialogFragment() {
    protected lateinit var viewModelFactory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = Injection.provideViewModelFactory(requireContext())

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogFragment)

//        val layoutParams = dialog?.window?.attributes
//        layoutParams?.y = (layoutParams?.y ?: 0) - 200
//        dialog?.window?.attributes = layoutParams
    }
}