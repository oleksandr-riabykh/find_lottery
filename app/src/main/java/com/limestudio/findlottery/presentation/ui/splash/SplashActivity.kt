package com.limestudio.findlottery.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.MainActivity
import com.limestudio.findlottery.presentation.ui.auth.AuthViewModel
import com.limestudio.findlottery.presentation.base.BaseActivity
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import com.limestudio.findlottery.presentation.ui.auth.AuthState
import com.limestudio.findlottery.presentation.ui.auth.CODE_USER_STATUS

class SplashActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModels { Injection.provideViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.hide()

        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)

        viewModel.state.observe(this, { state ->
            when (state) {
                is AuthState.StartMainActivity -> {
                    startActivity(Intent(this, MainActivity::class.java)
                        .putExtra(CODE_USER_STATUS, state.status))
                    finish()
                }
                is AuthState.StartRelogin -> {
                    Firebase.auth.signOut()
                    showWarning("Something went wrong, please re-login")
                }
                else -> {}
            }
        })
    }

    private val authStateListener =
        AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
            if (firebaseUser != null) {
                viewModel.checkUserStatus()
            }
        }
}
