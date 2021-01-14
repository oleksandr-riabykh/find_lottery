package com.limestudio.findlottery.presentation.services.locationservice

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager

class LocationProvider(context: Context) {

    companion object {
        private const val MIN_TIME_MS = 1000 * 60 * 10L
        private const val MIN_DISTANCE_M = 10f
    }

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(locationListener: LocationListener) {
        if (isGPSEnabled()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, MIN_TIME_MS, MIN_DISTANCE_M, locationListener
            )
        } else if (isNetworkEnabled()) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, MIN_TIME_MS, MIN_DISTANCE_M, locationListener
            )
        }
    }

    private fun isGPSEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    private fun isNetworkEnabled() = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}