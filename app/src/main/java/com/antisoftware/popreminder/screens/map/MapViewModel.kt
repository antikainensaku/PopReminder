package com.antisoftware.popreminder.screens.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antisoftware.popreminder.REMINDER_DEFAULT_ID
import com.antisoftware.popreminder.common.extension.idFromParameter
import com.antisoftware.popreminder.common.maps.ClusterManager
import com.antisoftware.popreminder.common.maps.calculateCameraViewPoints
import com.antisoftware.popreminder.common.maps.getCenterOfPolygon
import com.antisoftware.popreminder.common.workmanager.Notifications
import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.screens.PopReminderViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService
) : PopReminderViewModel(logService) {

    var uiState = mutableStateOf(MapUiState(lastKnownLocation = Location("Oulu"), spots = emptyList()))


//    init {
//        viewModelScope.launch {
//            storageService.getReminderSpots().collectLatest { spots ->
//                uiState.value = uiState.value.copy(
//                    spots = spots
//                )
//            }
//        }
//    }

//    fun onEvent(event: MapEvent) {
//        when(event) {
//            is MapEvent.ToggleFalloutMap -> {
//                state = state.copy(
//                    properties = state.properties.copy(
//                        mapStyleOptions = if(state.isFalloutMap) {
//                            null
//                        } else MapStyleOptions(MapStyle.json),
//                    ),
//                    isFalloutMap = !state.isFalloutMap
//                )
//            }
//            is MapEvent.OnMapLongClick -> {
//                viewModelScope.launch {
//                    repository.insertParkingSpot(ParkingSpot(
//                        event.latLng.latitude,
//                        event.latLng.longitude
//                    ))
//                }
//            }
//            is MapEvent.OnInfoWindowLongClick -> {
//                viewModelScope.launch {
//                    repository.deleteParkingSpot(event.spot)
//                }
//            }
//        }
//    }


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
                    uiState.value = uiState.value.copy(
                        lastKnownLocation = task.result,
                    )
                }
            }
        } catch (e: SecurityException) {
            // Show error or something
        }
    }
}

//@HiltViewModel
//class MapViewModel @Inject constructor(
//    logService: LogService,
//    private val storageService: StorageService,
//) : PopReminderViewModel(logService) {
//    var uiState = mutableStateOf(MapUiState(lastKnownLocation = Location("Oulu")))
//    val reminder = mutableStateOf(Reminder())
//    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Notifications.appContext)
//
//    fun initialize(reminderId: String) {
//        launchCatching {
//            if (reminderId != REMINDER_DEFAULT_ID) {
//                reminder.value = storageService.getReminder(reminderId.idFromParameter()) ?: Reminder()
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun getDeviceLocation(
//        fusedLocationProviderClient: FusedLocationProviderClient
//    ) {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            val locationResult = fusedLocationProviderClient.lastLocation
//            locationResult.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    uiState.value = uiState.value.copy(
//                        lastKnownLocation = task.result,
//                    )
//                }
//            }
//        } catch (e: SecurityException) {
//            // Show error or something
//        }
//    }
//
//    fun setupClusterManager(
//        context: Context,
//        map: GoogleMap,
//    ): ClusterManager {
//        val clusterManager = ClusterManager(context, map)
//        clusterManager.addItems(uiState.value.clusterItems)
//        return clusterManager
//    }
//
//    fun calculateZoneLatLngBounds(): LatLngBounds {
//        // Get all the points from all the polygons and calculate the camera view that will show them all.
//        val latLngs = uiState.value.clusterItems.map { it.polygonOptions }
//            .map { it.points.map { LatLng(it.latitude, it.longitude) } }.flatten()
//        return latLngs.calculateCameraViewPoints().getCenterOfPolygon()
//    }
//
//    fun onMapClick(latLng: LatLng) {
//        reminder.value = reminder.value.copy(latitude = latLng.latitude, longitude = latLng.longitude)
//    }
//
//    companion object {
//        private val POLYGON_FILL_COLOR = Color.parseColor("#ABF44336")
//    }
//}