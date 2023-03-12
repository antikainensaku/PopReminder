package com.antisoftware.popreminder.data

import com.antisoftware.popreminder.common.maps.getCenterOfPolygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.clustering.ClusterItem

data class Zone(
    val id: String = "",
    private val title: String = "",
    private val snippet: String = "",
    val polygonOptions: PolygonOptions = PolygonOptions()
) : ClusterItem {
    override fun getSnippet() = snippet
    override fun getTitle() = title
    override fun getPosition() = polygonOptions.points.getCenterOfPolygon().center
}