package com.limestudio.findlottery.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng

val Location.toLatLng: LatLng get() = LatLng(latitude, longitude)