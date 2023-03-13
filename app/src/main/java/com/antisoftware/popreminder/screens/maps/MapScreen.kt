package com.antisoftware.popreminder.screens.maps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "PotentialBehaviorOverride")
@Composable
fun MapScreen(
    popUpScreen: () -> Unit,
    viewModel: MapViewModel = hiltViewModel(),
) {
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
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(MapUiState.lastKnownLocation.latitude, MapUiState.lastKnownLocation.longitude), 10f)
            },
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLongClick = {
                viewModel.onMapLongClick(it)
            }
        ) {
            var latitude by remember { mutableStateOf(0.0) }
            var longitude by remember { mutableStateOf(0.0) }
            if (MapUiState.reminderSpot != LatLng(0.0,0.0)) {
                latitude = MapUiState.reminderSpot.latitude
                longitude = MapUiState.reminderSpot.longitude
                Marker(
                    state = MarkerState(position = MapUiState.reminderSpot),
                    title = "Reminder spot @ (${latitude}, ${longitude})",
                    snippet = "Long click to delete",
                    onInfoWindowLongClick = {
                        viewModel.onMarkerLongClick()
                    },
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    )
                )
            }
        }
    }
}