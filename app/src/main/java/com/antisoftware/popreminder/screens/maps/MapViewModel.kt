package com.antisoftware.popreminder.screens.maps

import android.annotation.SuppressLint
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService
) : PopReminderViewModel(logService) {

    fun onOkClick(popUpScreen: () -> Unit) {
        popUpScreen()
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    MapUiState.lastKnownLocation = task.result
                }
            }
        } catch (e: SecurityException) {
            // Show error or something
        }
    }
    fun onMarkerLongClick() {
        MapUiState.reminderSpot = LatLng(0.0,0.0)
    }

    fun onMapLongClick(location: LatLng) {
        MapUiState.reminderSpot = location
    }
}