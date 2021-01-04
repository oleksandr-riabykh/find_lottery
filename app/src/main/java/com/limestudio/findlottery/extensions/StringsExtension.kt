package com.limestudio.findlottery.extensions

import android.os.Build
import android.text.Html

fun String.platformFromHtml() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
} else {
    Html.fromHtml(this)
}

fun String.validateEmailAddress(): Boolean {
    return this.matches(Regex(".+@.+\\..+"))
}

fun String.containsNumber(): Boolean {
    return this.matches(Regex(".*\\d.*"))
}