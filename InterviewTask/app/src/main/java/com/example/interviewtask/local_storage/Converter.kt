package com.example.interviewtask.local_storage

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class Converters {

    companion object {
        var gson = Gson()

        @TypeConverter
        fun stringToSomeObjectList(data: String?): List<Int> {
            if (data == null) {
                return Collections.emptyList()
            }
            val listType: Type = object : TypeToken<List<Int?>?>() {}.type
            return gson.fromJson<List<Int>>(data, listType)
        }

        @TypeConverter
        fun someObjectListToString(someObjects: List<Int?>?): String {
            return gson.toJson(someObjects)
        }
    }
}