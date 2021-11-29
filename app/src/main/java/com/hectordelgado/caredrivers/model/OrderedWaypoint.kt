package com.hectordelgado.caredrivers.model

import androidx.room.Entity

@Entity
data class OrderedWaypoint(
    val id: Int,
    val anchor: Boolean,
    val passengers: List<Passenger>,
    val location: Location
)
