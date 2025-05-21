package dev.yovany.todoapp.presentation.home

import dev.yovany.todoapp.domain.Task

data class HomeDataState(
    val date: String = "",
    val summary: String = "",
    val completedTasks: List<Task> = emptyList(),
    val pendingTasks: List<Task> = emptyList(),
)