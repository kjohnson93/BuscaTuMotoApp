package com.buscatumoto.utils.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.Collections.emptyList


/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    @TypeConverter fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter fun stringList(list: List<String>): String {
        val gson = Gson()
        val json = gson.toJson(list)
        return json
    }

    @TypeConverter
    fun stringToSomeList(data: String?): List<String>? {
        if (data == null) {
            return emptyList()
        }

        return Gson().fromJson(
            data,
            object : TypeToken<List<String>?>() {}.type
        )
    }

    @TypeConverter fun intList(list: List<Int>): String {
        val gson = Gson()
        val json = gson.toJson(list)
        return json
    }

    @TypeConverter
    fun intToSomeList(data: String?): List<Int>? {
        if (data == null) {
            return emptyList()
        }

        return Gson().fromJson(
            data,
            object: TypeToken<List<Int>?>() {}.type
        )
    }

    @TypeConverter fun floatList(list: List<Float>): String {
        val gson = Gson()
        val json = gson.toJson(list)
        return json
    }

    @TypeConverter
    fun floatToSomeList(data: String?): List<Float>? {
        if (data == null) {
            return emptyList()
        }

        return Gson().fromJson(
            data,
            object : TypeToken<List<Float>?>() {}.type
        )
    }



//   @TypeConverter fun fromStringList(): List<String> {
//       val gson = Gson()
//
//   }
}