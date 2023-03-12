package com.antisoftware.popreminder.screens.map

import android.location.Location
import com.antisoftware.popreminder.data.Zone
import kotlinx.coroutines.flow.Flow

data class MapUiState(
    val lastKnownLocation: Location?,
    val clusterItems: List<Zone> = emptyList(),
    val spots: List<Location>
)