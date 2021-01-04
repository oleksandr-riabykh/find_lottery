package com.limestudio.findlottery.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.limestudio.findlottery.R

fun Fragment.navigateTo(
    to: Int,
    popTo: Int? = null,
    inclusive: Boolean = false,
    bundle: Bundle? = null
) {
    val options = NavOptions.Builder()
        .setEnterAnim(R.anim.nav_default_enter_anim)
        .setExitAnim(R.anim.nav_default_exit_anim)
        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(R.anim.nav_default_pop_exit_anim)

    popTo?.let { options.setPopUpTo(it, inclusive) }

    findNavController().navigate(to, bundle, options.build())
}

fun Fragment.navigateBack() {
    findNavController().navigateUp()
}