package com.limestudio.findlottery.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.limestudio.findlottery.R
import com.limestudio.findlottery.presentation.MainActivity
import com.limestudio.findlottery.presentation.base.BaseActivity
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity


class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.hide()

        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    var authStateListener =
        AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
            if (firebaseUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
}
