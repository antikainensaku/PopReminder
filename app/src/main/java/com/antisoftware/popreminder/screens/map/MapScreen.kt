package com.antisoftware.popreminder.screens.map


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antisoftware.popreminder.data.LocationObject
import com.antisoftware.popreminder.screens.edit.EditReminderViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.antisoftware.popreminder.R.drawable as AppIcon

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "PotentialBehaviorOverride")
@Composable
fun MapScreen(
    popUpScreen: () -> Unit,
    viewModel: MapViewModel = hiltViewModel(),
    reminderViewModel: EditReminderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val reminder by reminderViewModel.reminder

    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = true,
    )
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onOkClick(popUpScreen) },
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Check, "Ok/Continue")
            }
        }
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLongClick = {
                reminderViewModel.onMapLongClick(it)
            }
        ) {
            var latitude by remember { mutableStateOf(0.0) }
            var longitude by remember { mutableStateOf(0.0) }
            if (LocationObject.location != LatLng(0.0, 0.0)) {
                latitude = LocationObject.location.latitude
                longitude = LocationObject.location.longitude
                Marker(
                    state = MarkerState(position = LocationObject.location),
                    title = "Reminder spot @ (${latitude}, ${longitude})",
                    snippet = "Long click to delete",
                    onInfoWindowLongClick = {
                        reminderViewModel.onMarkerLongClick()
                    },
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                )
            }
        }
    }
}