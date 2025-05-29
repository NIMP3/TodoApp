package dev.yovany.todoapp.data

import android.content.Context
import dev.yovany.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher

object DataSourceFactory {
    fun createDataSource(
        context: Context,
        dispatcher: CoroutineDispatcher
    ): TaskLocalDataSource {
        val database = TodoDatabase.getDatabase(context)
        return RoomTaskLocalDataSource(database.taskDao(), dispatcher )
    }
}