package dev.yovany.todoapp

import android.app.Application
import dev.yovany.todoapp.data.DataSourceFactory
import dev.yovany.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TodoApplication: Application() {
    val dispatcherIO: CoroutineDispatcher
        get() = Dispatchers.IO

    val dataSource: TaskLocalDataSource
        get() = DataSourceFactory.createDataSource(this, dispatcherIO)
}