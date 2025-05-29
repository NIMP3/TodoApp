package dev.yovany.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
       @Volatile
       private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "task_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}