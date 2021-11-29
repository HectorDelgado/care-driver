package com.hectordelgado.caredrivers.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hectordelgado.caredrivers.model.Ride

@Database(entities = [Ride::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ride-database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    abstract fun rideDao(): RideDao




}