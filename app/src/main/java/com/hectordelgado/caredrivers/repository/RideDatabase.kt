package com.hectordelgado.caredrivers.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.util.Converters

/**
 * A Room database that provides a thread-safe singleton implementation.
 */
@Database(entities = [Ride::class], version = 1)
@TypeConverters(Converters::class)
abstract class RideDatabase : RoomDatabase() {
    companion object {
        @Volatile private var INSTANCE: RideDatabase? = null

        fun getInstance(context: Context): RideDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RideDatabase::class.java,
                    "ride-database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun rideDao(): RideDao
}