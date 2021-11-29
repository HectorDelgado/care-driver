package com.hectordelgado.caredrivers.util

/**
 * Represents an error from a Response object.
 * @param code The error code.
 * @param body A message from the error.
 */
data class ResponseError(val code: Int, val body: String?)
