package com.limestudio.findlottery.presentation

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.limestudio.findlottery.BuildConfig
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.showAlertNegative
import com.limestudio.findlottery.presentation.ui.auth.CODE_USER_TYPE
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if (intent?.extras?.getInt(CODE_USER_TYPE) == 0)
            navView.menu.findItem(R.id.navigation_drawsFragment).isVisible = false
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_map,
                R.id.navigation_drawsFragment,
                R.id.navigation_messages,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        handleRemoteConfigVersion()
    }

    private fun handleRemoteConfigVersion() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(600)
            .build()
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        val defaults: MutableMap<String, Any> = HashMap()
        defaults[KEY_APP_VERSION] = "1.0.0"
        firebaseRemoteConfig.setDefaultsAsync(defaults)
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) {}
        val remoteVersion = FirebaseRemoteConfig.getInstance().getString(KEY_APP_VERSION)
        if (BuildConfig.VERSION_NAME != remoteVersion) {
            showAlertNegative(
                getString(R.string.title_version_popup),
                getString(R.string.description_version_popup),
                {}) {
                val browserIntent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://findlottery.crystalweb-asia.com/")
                    )
                startActivity(browserIntent)
            }
        }
    }

    companion object {
        private const val KEY_APP_VERSION = "app_version"
    }
}
