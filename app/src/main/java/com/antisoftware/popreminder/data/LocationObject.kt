package com.antisoftware.popreminder.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng

object LocationObject {
    var location by mutableStateOf(LatLng(0.0,0.0))
}