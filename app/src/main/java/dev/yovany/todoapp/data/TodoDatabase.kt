package dev.yovany.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TaskEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}