package com.example.bottombar.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bottombar.util.Converters

@Database(
    entities = [Task::class],
    version = 7
)
@TypeConverters(Converters::class)
abstract class TaskDatabase: RoomDatabase() {

    abstract val dao: TaskDao
}