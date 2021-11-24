package com.hectordelgado.caredrivers.network

class ApiHelper(private val apiService: ApiService) {
    suspend fun fetchRides() = apiService.fetchRides()
}