package com.limestudio.findlottery.presentation.base

import androidx.appcompat.app.AppCompatActivity
import com.limestudio.findlottery.presentation.ui.ViewModelFactory

open class BaseActivity : AppCompatActivity() {
    protected lateinit var viewModelFactory: ViewModelFactory
}