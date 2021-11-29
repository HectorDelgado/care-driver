package com.hectordelgado.caredrivers.repository

import com.hectordelgado.caredrivers.network.ApiService

/**
 * Data access object for interacting with [ApiService].
 * @param apiService an Instance of ApiService
 */
class ApiDao(private val apiService: ApiService) {
    suspend fun fetchRides() = apiService.fetchRides()
}