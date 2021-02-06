package com.limestudio.findlottery.presentation.ui.fullscreen_photo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.limestudio.findlottery.R
import kotlinx.android.synthetic.main.activity_fullscreen_photo.*

class FullscreenPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen_photo)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        intent?.extras?.getString(ARG_PHOTO)?.let {
            Glide.with(this).load(it).into(image_id_card)
        }
    }

    companion object {
        const val ARG_PHOTO = "photo_id"
    }
}