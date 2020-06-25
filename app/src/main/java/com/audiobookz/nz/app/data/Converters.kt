package com.audiobookz.nz.app.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {

    @TypeConverter fun listToString(value: List<String>?) = value?.joinToString()?:"test123"

    @TypeConverter fun stringToList(value: String):List<String> {
        return value.split(',')
    }

}