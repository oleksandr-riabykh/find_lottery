package com.limestudio.findlottery.presentation.ui.auth

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.limestudio.findlottery.R
import com.limestudio.findlottery.presentation.base.BaseActivity

class AuthActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val navController = findNavController(R.id.auth_nav_host_fragment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupActionBarWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> super.onOptionsItemSelected(item);
        }
        return true
    }

    fun hideToolbar() = supportActionBar?.hide()
    fun showToolbar() = supportActionBar?.show()
}
