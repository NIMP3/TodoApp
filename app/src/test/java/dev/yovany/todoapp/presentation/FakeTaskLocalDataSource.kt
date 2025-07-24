package dev.yovany.todoapp.presentation

import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeTaskLocalDataSource: TaskLocalDataSource {
    private  val _tasksFlow = MutableStateFlow<List<Task>>(emptyList())
    override val taskFlow: Flow<List<Task>> = _tasksFlow.asStateFlow()

    private val currentTasks = mutableListOf<Task>()

    override suspend fun addTask(task: Task) {
        currentTasks.add(task)
        _tasksFlow.update { currentTasks.toList() }
    }

    override suspend fun updateTask(task: Task) {
        val index = currentTasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            currentTasks[index] = task
            _tasksFlow.update { currentTasks.toList() }
        }
    }

    override suspend fun removeTask(task: Task) {
        currentTasks.removeIf { it.id == task.id }
        _tasksFlow.update { currentTasks.toList() }
    }

    override suspend fun removeAllTasks() {
        currentTasks.clear()
        _tasksFlow.update { currentTasks.toList() }
    }

    override suspend fun getTaskById(id: String): Task? = currentTasks.find { it.id == id }

    fun setTasks(tasks: List<Task>) {
        currentTasks.clear()
        currentTasks.addAll(tasks)
        _tasksFlow.value = currentTasks.toList()
    }

    fun loadTestTasks() {
        val testTasks = listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                categories = listOf(Category.WORK),
            ),
            Task(
                id = "2",
                title = "Task 2",
                description = "Description 2",
                isCompleted = true,
                categories = listOf(Category.OTHER)
            )
        )

        setTasks(testTasks)
    }

    fun getCurrentTasksSnapshot(): List<Task> = currentTasks.toList()

}