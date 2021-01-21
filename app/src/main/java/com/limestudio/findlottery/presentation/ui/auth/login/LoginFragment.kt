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
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.MainActivity
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import com.limestudio.findlottery.presentation.ui.auth.AuthState
import com.limestudio.findlottery.presentation.ui.auth.AuthViewModel
import com.limestudio.findlottery.presentation.ui.auth.CODE_USER_TYPE
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private val viewModel: AuthViewModel by viewModels { Injection.provideViewModelFactory(requireContext()) }

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

        viewModel.state.observe(requireActivity(), { state ->
            when (state) {
                is AuthState.StartMainActivity -> {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                        .putExtra(CODE_USER_TYPE, state.status)
                    startActivity(intent)
                    requireActivity().finish()
                }
                is AuthState.StartRelogin -> {
                    showWarning(R.string.please_relogin)
                    email.text.clear()
                    password.text.clear()
                    password.clearFocus()
                }
                else -> {}
            }
        })

        login_button.setOnClickListener {
            val userEmail = email?.text.toString()
            val pass = password?.text.toString()
            if (userEmail.isBlank() || pass.isBlank()) {
                showWarning("Email or password shouldn't be empty")
            } else {
                auth.signInWithEmailAndPassword(userEmail, pass)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            viewModel.checkUserType()
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
