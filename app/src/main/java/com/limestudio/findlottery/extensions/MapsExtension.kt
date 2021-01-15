package com.limestudio.findlottery.extensions

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.limestudio.findlottery.presentation.ui.map.ZOOM_LEVEL

fun GoogleMap.zoomCamera(latLng: LatLng, zoomLevel: Float = ZOOM_LEVEL) {
    try {
        this.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, zoomLevel
            )
        )
    } catch (e: Exception) {
        FirebaseCrashlytics.getInstance().recordException(e)
    }
}