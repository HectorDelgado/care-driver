package com.hectordelgado.caredrivers.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.util.Converters

@Database(entities = [Ride::class], version = 1)
@TypeConverters(Converters::class)
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