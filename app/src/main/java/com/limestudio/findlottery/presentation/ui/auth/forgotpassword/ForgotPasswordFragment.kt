package com.limestudio.findlottery.presentation.ui.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.navigateBack
import com.limestudio.findlottery.extensions.showToast
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.extensions.validateEmailAddress
import com.limestudio.findlottery.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPasswordFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_forgot_password, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        send_button.setOnClickListener {
            val userEmail = email?.text.toString()
            if (userEmail.isBlank() || !userEmail.validateEmailAddress()) {
                email.error = getString(R.string.signup_invalid_email_error_message)
            } else {
                email.error = null
                FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast(R.string.forgot_success)
                            navigateBack()
                        } else {
                            showWarning(R.string.error_unknown)
                        }
                    }
            }

        }
    }
}
