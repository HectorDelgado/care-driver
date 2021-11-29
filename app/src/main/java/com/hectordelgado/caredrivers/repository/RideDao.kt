package com.hectordelgado.caredrivers.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hectordelgado.caredrivers.model.Ride

@Dao
interface RideDao {
    @Query("SELECT * FROM ride WHERE tripId LIKE (:id)")
    suspend fun getById(id: String): Ride

    @Insert
    suspend fun insert(ride: Ride)

    @Query("DELETE FROM ride")
    suspend fun deleteAll()
}