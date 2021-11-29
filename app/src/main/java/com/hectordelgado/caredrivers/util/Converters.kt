package com.hectordelgado.caredrivers.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hectordelgado.caredrivers.model.OrderedWaypoint

class Converters {
    @TypeConverter
    fun fromList(list: List<OrderedWaypoint>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(str: String): List<OrderedWaypoint> {
        val listType = object : TypeToken<List<OrderedWaypoint>>() {}.type
        return Gson().fromJson(str, listType)
    }
}