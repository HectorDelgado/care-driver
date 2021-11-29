package com.hectordelgado.caredrivers.model

sealed class Trip(val viewType: Int) {
    data class TripHeader(
        val tripDate: String,
        val startTime: String,
        val endTime: String,
        val estimatedTotal: String
    ) : Trip(0)

    data class TripCard(val ride: Ride) : Trip(1)
}
