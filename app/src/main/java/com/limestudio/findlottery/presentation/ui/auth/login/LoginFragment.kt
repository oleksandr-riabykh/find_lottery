package com.limestudio.findlottery.presentation.ui.auth.login

import android.content.Intent
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.MainActivity
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AuthActivity).showToolbar()
        forgot_password_textView.setOnClickListener {
            navigateTo(
                R.id.navigation_forgot_password,
                R.id.navigation_login
            )
        }
        login_button.setOnClickListener {
            val userEmail = email?.text.toString()
            val pass = password?.text.toString()
            if (userEmail.isBlank() || pass.isBlank()) {
                showWarning("Email or password shouldn't be empty")
            } else {
                auth.signInWithEmailAndPassword(userEmail, pass)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        } else {
                            showWarning("Authentication failure: Please, make you have an account")
                        }
                    }
            }
        }

        val greeting = getString(R.string.login_greeting)
        val spannable = SpannableString(greeting)
        spannable.setSpan(
            StyleSpan(BOLD),
            greeting.length - 12, greeting.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.orange, null)),
            greeting.length - 12, greeting.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        subtitle_text_view.text = spannable
    }
}
