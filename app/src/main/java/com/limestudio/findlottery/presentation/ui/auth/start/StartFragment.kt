package com.limestudio.findlottery.presentation.ui.auth.start

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_start.*
import kotlin.random.Random

class StartFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_start, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logo.setImageResource(
            arrayOf(
                R.drawable.ic_splash_0,
                R.drawable.ic_splash_1,
                R.drawable.ic_splash_2
            )[Random.nextInt(3)]
        )
        login_button.setOnClickListener { navigateTo(R.id.navigation_login, R.id.navigation_start) }
        signup_as_seller_button.setOnClickListener {
            navigateTo(
                R.id.navigation_signup_as_seller,
                R.id.navigation_start
            )
        }
        signup_as_guest_button.setOnClickListener {
            navigateTo(
                R.id.navigation_signup_as_guest,
                R.id.navigation_start
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
