package com.hectordelgado.caredrivers.repository

/**
 * Repository for accessing and interacting with elements from the [ApiDao].
 * @param apiDao Dao for the API
 */
class ApiRepository(private val apiDao: ApiDao) {
    suspend fun fetchRides() = apiDao.fetchRides()
}