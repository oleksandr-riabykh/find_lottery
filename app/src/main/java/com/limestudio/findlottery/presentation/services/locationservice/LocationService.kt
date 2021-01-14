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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationService : Service() {

    private val firebaseManager = FirebaseManager(null)

    private lateinit var locationProvider: LocationProvider

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        locationProvider = LocationProvider(this)

        Firebase.auth.currentUser?.uid?.let {
            locationProvider.requestLocationUpdates { location ->
                GlobalScope.launch(Dispatchers.IO) {
                    firebaseManager.updateUserLocation(it, location.toLatLng)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}