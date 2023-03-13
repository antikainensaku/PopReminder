package com.antisoftware.popreminder.screens.maps

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng

object MapUiState {
    var lastKnownLocation by mutableStateOf(Location("gps"))
    var reminderSpot by mutableStateOf(LatLng(0.0,0.0))
}

