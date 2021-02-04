package com.limestudio.findlottery.presentation.ui.fullscreen_photo

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.limestudio.findlottery.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fullscreen_photo.*

class FullscreenPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen_photo)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.hide()
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        intent?.extras?.getString(ARG_PHOTO)?.let {
            Picasso.get().load(it).into(image_id_card)
        }
    }

    companion object {
        const val ARG_PHOTO = "photo_id"
    }
}