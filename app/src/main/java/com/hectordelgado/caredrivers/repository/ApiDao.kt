package com.hectordelgado.caredrivers.repository

import com.hectordelgado.caredrivers.network.ApiService

class ApiDao(private val apiService: ApiService) {
    suspend fun fetchRides() = apiService.fetchRides()
}