package com.hectordelgado.caredrivers.model

import com.squareup.moshi.Json

data class Passenger(
    val id: Int,
    @Json(name = "booster_seat") val boosterSeat: Boolean,
    @Json(name = "first_name") val firstName: String
)
