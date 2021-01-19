package com.limestudio.findlottery.presentation.ui.auth.signup.guest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.bigkoo.svprogresshud.SVProgressHUD
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.AppLocation
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.extensions.showToast
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import com.limestudio.findlottery.presentation.ui.auth.signup.ImageModel
import com.limestudio.findlottery.presentation.ui.auth.signup.SignUpScreenState
import com.limestudio.findlottery.presentation.ui.auth.signup.SignUpViewModel
import com.limestudio.findlottery.presentation.ui.onboarding.OnboardingActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_signup_as_seller.*

const val CODE_AVATAR = 1
const val CODE_CARD = 2

class SignUpAsGuestFragment : BaseFragment(), OnCompleteListener<AuthResult> {

    private val viewModel: SignUpViewModel by viewModels { viewModelFactory }
    private lateinit var auth: FirebaseAuth
    private lateinit var images: HashMap<Int, ImageModel?>
    private lateinit var loadingIndicator: SVProgressHUD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        viewModelFactory = Injection.provideViewModelFactory(requireContext())
        images = hashMapOf()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                data?.data?.let { uri ->
                    var folder = "avatar"
                    if (requestCode == CODE_AVATAR) {
                        Picasso.get().load(uri).transform(CropCircleTransformation()).into(avatar)
                        folder = "avatar"
                    } else if (requestCode == CODE_CARD) {
                        folder = "idcard"
                        id_card.adjustViewBounds = true
                        id_card.scaleType = ImageView.ScaleType.CENTER_CROP
                        id_card.setPadding(0, 0, 0, 0)
                        Picasso.get().load(uri).into(id_card)

                    }
                    images[requestCode] = ImageModel(
                        uri,
                        folder
                    )
                }

            }
            ImagePicker.RESULT_ERROR -> {
                showWarning(ImagePicker.getError(data))
            }
            else -> {
                showToast("Task Cancelled")
            }
        }
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
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)

                    activity?.finishAndRemoveTask();
                }
                is SignUpScreenState.FilesUploaded -> {
                    auth.currentUser?.let { firebaseUser ->
                        viewModel.addUser(
                            User(
                                id = firebaseUser.uid,
                                name = first_name?.text.toString(),
                                lastName = last_name?.text.toString(),
                                phoneNumber = phone_number?.text.toString(),
                                city = city?.selectedItem.toString().toLowerCase(),
                                nationalId = national_id?.text.toString(),
                                location = AppLocation(10.23, 120.42)
//                                location = hashMapOf()
                            )
                        )
                    }
                }
                is SignUpScreenState.UploadError -> {
                    showWarning("Error to upload the file")
                    if (loadingIndicator.isShowing) loadingIndicator.dismiss()
                }
            }
        })
    }

    override fun onComplete(authResult: Task<AuthResult>) {
        if (authResult.isSuccessful) {
            if (auth.currentUser != null) {
                viewModel.uploadImages(auth.currentUser!!.uid, images)
            } else {
                navigateTo(
                    R.id.navigation_login,
                    R.id.navigation_start, false
                )
                if (loadingIndicator.isShowing) loadingIndicator.dismiss()
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
