package com.hectordelgado.caredrivers.repository

class ApiRepository(private val apiDao: ApiDao) {
    suspend fun fetchRides() = apiDao.fetchRides()
}