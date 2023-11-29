package com.example.bottombar.util

import androidx.room.TypeConverter
import com.example.bottombar.data.room.Subtask
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromList(value: List<Subtask>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<Subtask>>(value)

}