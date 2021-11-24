package com.hectordelgado.caredrivers.model

data class OrderedWaypoint(
    val id: Int,
    val anchor: Boolean,
    val passengers: List<Passenger>,
    val location: Location
)
