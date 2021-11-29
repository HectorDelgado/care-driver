package com.hectordelgado.caredrivers.util

import java.time.*
import java.time.format.TextStyle
import java.util.*

/**
 * Converts a number of cents into a formatted string representing dollars and
 * cents (without a symbol).
 * @return A formatted version of the cents.
 */
fun Int.centsToDollarsAndCents(): String {
    val dollars = this/100
    val cents = "%02d".format(this - (dollars * 100))
    return "$dollars.$cents"
}

/**
 * Converts a ISO 8601 compliant string into a LocalDateTime object.
 * @param zoneId The zoneID to use for parsing.
 * @return A LocalDateTime object representing an ISO 8601 time.
 */
fun String.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.parse(this), zoneId)
}

/**
 * Converts a LocalDateTime object into a formatted 12-hour string.
 * @return A string formatted in HH:mm.
 */
fun LocalDateTime.toFormattedTime(): String {
    val hour = if (this.hour < 13) this.hour else this.hour % 12
    val minute = this.minute
    val timeAbbreviation = if (this.hour < 12) "a" else "p"
    return "$hour:$minute$timeAbbreviation"
}

/**
 * Convenience method to return the short name of a [DayOfWeek] object.
 */
fun DayOfWeek.toShortName(locale: Locale = Locale.getDefault()): String {
    return this.getDisplayName(TextStyle.SHORT, locale)
}