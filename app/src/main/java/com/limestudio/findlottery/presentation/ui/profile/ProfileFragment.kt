package com.limestudio.findlottery.presentation.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout_button.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }
}
