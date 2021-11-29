package com.hectordelgado.caredrivers.util

import java.time.*
import java.time.format.TextStyle
import java.util.*

fun Int.centsToDollarsAndCents(): String {
    val dollars = this/100
    val cents = "%02d".format(this - (dollars * 100))
    return "$dollars.$cents"
}

fun String.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.parse(this), zoneId)
}

fun LocalDateTime.toFormattedTime(): String {
    val hour = if (this.hour < 13) this.hour else this.hour % 12
    val minute = this.minute
    val timeAbbreviation = if (this.hour < 12) "a" else "p"
    return "$hour:$minute$timeAbbreviation"
}

fun DayOfWeek.toShortName(locale: Locale = Locale.getDefault()): String {
    return this.getDisplayName(TextStyle.SHORT, locale)
}