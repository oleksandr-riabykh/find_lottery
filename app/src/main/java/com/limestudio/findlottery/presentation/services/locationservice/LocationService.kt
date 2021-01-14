package com.limestudio.findlottery.presentation.services.locationservice

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.IBinder
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.data.firebase.FirebaseManager
import com.limestudio.findlottery.extensions.toLatLng
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationService : Service() {

    private val firebaseManager = FirebaseManager(null)

    private lateinit var locationProvider: LocationProvider

    override fun onCreate() {
        locationProvider = LocationProvider(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startLocationUpdates() {
        Firebase.auth.currentUser?.uid?.let { uid ->
            locationProvider.requestLocationUpdates { location ->
                GlobalScope.launch(Dispatchers.IO) {
                    firebaseManager.updateUserLocation(uid, location.toLatLng)
                }
            }
        }
    }
}