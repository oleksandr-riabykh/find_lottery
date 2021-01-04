package com.limestudio.findlottery.presentation.ui.auth.start

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.presentation.MainActivity
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_start, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login_button.setOnClickListener { navigateTo(R.id.navigation_login, R.id.navigation_start) }
        sigup_button.setOnClickListener {
            navigateTo(
                R.id.navigation_signup,
                R.id.navigation_start
            )
        }
        guest_button.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    MainActivity::class.java
                )
            )
        }

        privacy_textView.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://user.teana.limestudio.us/terms"))
            requireActivity().startActivity(browserIntent)
        }
        (requireActivity() as AuthActivity).hideToolbar()
    }
}
