package com.hectordelgado.caredrivers

import com.hectordelgado.caredrivers.network.ApiHelper
import com.hectordelgado.caredrivers.network.ApiService

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun fetchRides() = apiHelper.fetchRides()
}