package com.hectordelgado.caredrivers

fun Int.centsToDollarsAndCents(): String {
    val dollars = this/100
    val cents = "%02d".format(this - (dollars * 100))
    return "$dollars.$cents"
}