package com.hectordelgado.caredrivers.model

sealed class Trip(val viewType: Int) {
    data class TripHeader(
        val tripDate: String,
        val startTime: String,
        val endTime: String,
        val estimatedTotal: String
    ) : Trip(0)

    data class TripCard(
        val startTime: String,
        val endTime: String,
        val riderBoosterDescription: String,
        val tripEstimate: String,
        val waypoints: List<OrderedWaypoint>
    ) : Trip(1)
}
