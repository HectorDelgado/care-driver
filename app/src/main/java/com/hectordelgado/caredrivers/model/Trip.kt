package com.hectordelgado.caredrivers.model

import java.time.LocalDateTime

sealed class Trip(val viewType: Int) {
    data class TripHeader(
        val tripDate: String,
        val startTime: String,
        val endTime: String,
        val estimatedTotal: String
    ) : Trip(0)

//    data class TripCard(
//        val startLdt: LocalDateTime,
//        val endLdt: LocalDateTime,
//        val startTime: String,
//        val endTime: String,
//        val riderBoosterDescription: String,
//        val tripEstimate: String,
//        val waypoints: List<OrderedWaypoint>
//    ) : Trip(1)

    data class TripCard(val ride: Ride) : Trip(1)
}
