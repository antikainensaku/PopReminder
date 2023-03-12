package com.antisoftware.popreminder.data

import com.google.android.gms.maps.model.LatLng

data class Spot(
    val location: LatLng = LatLng(0.0, 0.0)
)
