package com.hectordelgado.caredrivers.model

import com.squareup.moshi.Json

data class Ride(
    @Json(name = "trip_id") val tripId: Int,
    @Json(name = "in_series") val inSeries: Boolean,
    @Json(name = "starts_at") val startsAt: String,
    @Json(name = "ends_at") val endsAt: String,
    @Json(name = "estimated_earnings_cents") val estimatedEarningsCents: Int,
    @Json(name = "estimated_ride_minutes") val estimatedRideMinutes: Int,
    @Json(name = "estimated_ride_miles") val estimatedRideMiles: Double,
    @Json(name = "ordered_waypoints") val orderedWaypoints: List<OrderedWaypoint>
)
