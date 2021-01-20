package com.limestudio.findlottery.presentation.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.limestudio.findlottery.R
import com.limestudio.findlottery.presentation.MainActivity
import com.limestudio.findlottery.presentation.ui.auth.CODE_USER_STATUS
import kotlinx.android.synthetic.main.activity_onboarding.done_button as doneButton
import kotlinx.android.synthetic.main.activity_onboarding.skip_label as skipLabel
import kotlinx.android.synthetic.main.activity_onboarding.tab_layout as tabLayout
import kotlinx.android.synthetic.main.activity_onboarding.viewpager as viewPager

class OnboardingActivity : AppCompatActivity(), (Boolean) -> Unit {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.hide()
        viewPager.adapter = OnboardingPager(supportFragmentManager, lifecycle)

        viewPager.registerOnPageChangeCallback(ViewPager2PageChangeCallback(this))
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        doneButton.setOnClickListener {
            startActivity(
                Intent(applicationContext, MainActivity::class.java)
                    .putExtra(CODE_USER_STATUS, intent.extras?.getInt(CODE_USER_STATUS) ?: 0)
            )
        }
        skipLabel.setOnClickListener {
            startActivity(
                Intent(applicationContext, MainActivity::class.java)
                    .putExtra(CODE_USER_STATUS, intent.extras?.getInt(CODE_USER_STATUS) ?: 0)
            )
        }
    }

    override fun invoke(show: Boolean) =
        if (show) doneButton.visibility = View.VISIBLE else doneButton.visibility = View.INVISIBLE
}

class ViewPager2PageChangeCallback(private val listener: (Boolean) -> Unit) :
    ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        when (position) {
            4 -> listener.invoke(true)
            else -> listener.invoke(false)

        }
    }
}