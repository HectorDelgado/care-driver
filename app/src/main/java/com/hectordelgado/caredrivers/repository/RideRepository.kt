package com.hectordelgado.caredrivers.repository

import com.hectordelgado.caredrivers.model.Ride

/**
 * Repository for accessing and interacting with elements from the [RideDao].
 * @param rideDao Dao for the ride database.
 */
class RideRepository(private val rideDao: RideDao) {
    suspend fun getById(id: Int) = rideDao.getById(id)

    suspend fun insert(ride: Ride) {
        rideDao.insert(ride)
    }

    suspend fun deleteAll() {
        rideDao.deleteAll()
    }
}