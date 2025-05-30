package dev.yovany.todoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.yovany.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@HiltAndroidApp
class TodoApplication: Application()