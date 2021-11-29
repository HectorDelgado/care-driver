package com.hectordelgado.caredrivers.network

import com.hectordelgado.caredrivers.model.HSDRide
import retrofit2.Response
import retrofit2.http.GET

/**
 * Provides an abstract definition of an API service.
 */
interface ApiService {
    // Gets out 'test' response data
    @GET("simplified_my_rides_response.json")
    suspend fun fetchRides(): Response<HSDRide>
}