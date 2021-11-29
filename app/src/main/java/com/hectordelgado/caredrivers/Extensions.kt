package com.hectordelgado.caredrivers

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Int.centsToDollarsAndCents(): String {
    val dollars = this/100
    val cents = "%02d".format(this - (dollars * 100))
    return "$dollars.$cents"
}

fun String.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.parse(this), zoneId)
}