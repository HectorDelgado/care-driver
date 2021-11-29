package com.hectordelgado.caredrivers.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RideDetail(
    @PrimaryKey val tripId: Int,
    val date: String,
    val startTime: String,
    val endTime: String,
    val rideEstimate: String,

    )