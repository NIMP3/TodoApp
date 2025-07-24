package dev.yovany.todoapp.domain

import java.time.LocalDateTime

data class Task(
    val id: String,
    val title: String,
    val description: String?,
    val isCompleted: Boolean = false,
    val categories: List<Category> = emptyList(),
    val date: LocalDateTime = LocalDateTime.now()
)
