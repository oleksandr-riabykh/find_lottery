package com.limestudio.findlottery.presentation.ui.auth.signup.guest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.bigkoo.svprogresshud.SVProgressHUD
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.UserType
import com.limestudio.findlottery.data.models.AppLocation
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.showToast
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import com.limestudio.findlottery.presentation.ui.auth.CODE_USER_TYPE
import com.limestudio.findlottery.presentation.ui.auth.signup.SignUpScreenState
import com.limestudio.findlottery.presentation.ui.auth.signup.SignUpViewModel
import com.limestudio.findlottery.presentation.ui.onboarding.OnboardingActivity
import kotlinx.android.synthetic.main.fragment_signup_as_seller.*


class SignUpAsGuestFragment : BaseFragment(), OnCompleteListener<AuthResult> {

    private val viewModel: SignUpViewModel by viewModels { viewModelFactory }
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingIndicator: SVProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        loadingIndicator = SVProgressHUD(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_signup_as_guest, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AuthActivity).showToolbar()
        initSubscribe()
        signup_button.setOnClickListener {
            if (isSignUpDataValid()) {
                val userEmail = email?.text.toString()
                val pass = password?.text.toString()

                auth.createUserWithEmailAndPassword(userEmail, pass)
                    .addOnCompleteListener(requireActivity(), this)
                if (!loadingIndicator.isShowing) loadingIndicator.show()
            }
        }
        validateSignUpData()
    }

    private fun initSubscribe() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is SignUpScreenState.ShowProgressBar -> if (!loadingIndicator.isShowing) loadingIndicator.show()
                is SignUpScreenState.HideProgressBar -> if (loadingIndicator.isShowing) loadingIndicator.dismiss()
                is SignUpScreenState.ShowMessage -> {
                    showToast(state.messageId)
                }
                is SignUpScreenState.ShowMessageString -> {
                    showToast(state.message)
                    if (loadingIndicator.isShowing) loadingIndicator.dismiss()
                }
                is SignUpScreenState.UserSaved -> {
                    if (loadingIndicator.isShowing) loadingIndicator.dismiss()
                    val intent = Intent(requireActivity(), OnboardingActivity::class.java)
                        .putExtra(CODE_USER_TYPE, UserType.GUEST.value)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    activity?.finishAndRemoveTask();
                }
                else -> {
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, { error ->
            if (loadingIndicator.isShowing) loadingIndicator.dismiss()
            showWarning(error.message)
        })
    }

    override fun onComplete(authResult: Task<AuthResult>) {
        if (authResult.isSuccessful) {
            auth.currentUser?.let {
                viewModel.addUser(
                    User(
                        id = it.uid,
                        name = first_name?.text.toString(),
                        lastName = last_name?.text.toString(),
                        location = AppLocation(10.23, 120.42),
                        type = UserType.GUEST.value
                    )
                )
            }
        } else {
            if (loadingIndicator.isShowing) loadingIndicator.dismiss()
            showWarning(R.string.error_unknown)
        }
    }

    data class Field(
        val field: EditText?,
        val errorMessage: String,
        val validation: (String) -> Boolean
    ) {
        fun validate(): Boolean {
            val isValid = field?.let {
                validation(it.text.toString())
            }
            if (isValid != true) {
                field?.error = errorMessage
                return false
            }
            return true
        }
    }

    private fun isSignUpDataValid(): Boolean {
        return listOf(
            Field(
                first_name,
                getString(R.string.signup_empty_field_error_message)
            ) { it.isNotEmpty() },
            Field(
                last_name,
                getString(R.string.signup_empty_field_error_message)
            ) { it.isNotEmpty() },
            Field(email, getString(R.string.signup_empty_field_error_message)) { it.isNotEmpty() },
            Field(
                password,
                getString(R.string.signup_empty_field_error_message)
            ) { it.isNotEmpty() },
            Field(
                password,
                getString(R.string.signup_password_helper_text)
            ) { viewModel.isPasswordContainsNumber(it) },
            Field(
                email,
                getString(R.string.signup_invalid_email_error_message)
            ) { viewModel.validateEmailAddress(it) }
        ).map { it.validate() }.all { it }
    }

    private fun validateSignUpData() {
        first_name?.doOnTextChanged { text, _, _, _ ->
            if (text?.isEmpty() == true)
                first_name.error = getString(R.string.signup_empty_field_error_message)
            else first_name.error = null
        }
        last_name?.doOnTextChanged { text, _, _, _ ->
            if (text?.isEmpty() == true)
                last_name.error = getString(R.string.signup_empty_field_error_message)
            else last_name.error = null
        }
        email?.doOnTextChanged { text, _, _, _ ->
            if (text?.isEmpty() == true)
                email.error = getString(R.string.signup_empty_field_error_message)
            else email.error = null
        }
        email?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if ((email?.text?.isNotEmpty() == true) && !hasFocus) {
                if (!viewModel.validateEmailAddress(email?.text.toString()))
                    email.error = getString(R.string.signup_invalid_email_error_message)
            }
        }
        password?.doOnTextChanged { text, _, _, _ ->
            if (text?.isEmpty() == true) {
                password.error = getString(R.string.signup_empty_field_error_message)
            } else {
                password.error = null
            }
        }
    }
}
