package com.antisoftware.popreminder.common.maps

import android.content.Context
import com.antisoftware.popreminder.data.Zone
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager

class ClusterManager(
    context: Context,
    googleMap: GoogleMap,
): ClusterManager<Zone>(context, googleMap, MarkerManager(googleMap))