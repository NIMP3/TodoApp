package dev.yovany.todoapp.data

import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTaskLocalDataSource: TaskLocalDataSource {
    private val _taskFlow = MutableStateFlow<List<Task>>(emptyList())

    override val taskFlow: Flow<List<Task>>
        get() = _taskFlow

    override suspend fun addTask(task: Task) {
        val tasks = _taskFlow.value.toMutableList()
        tasks.add(task)
        delay(200)
        _taskFlow.value = tasks
    }

    override suspend fun updateTask(task: Task) {
        val tasks = _taskFlow.value.toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            delay(200)
            _taskFlow.value = tasks
        }
    }

    override suspend fun removeTask(task: Task) {
        val tasks = _taskFlow.value.toMutableList()
        tasks.remove(task)
        delay(200)
        _taskFlow.value = tasks
    }

    override suspend fun getTaskById(id: String): Task? {
        return _taskFlow.value.firstOrNull { it.id == id }
    }
}