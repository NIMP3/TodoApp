package dev.yovany.todoapp.presentation.detail

import androidx.compose.foundation.text.input.TextFieldState
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Task
import java.util.UUID

data class TaskScreenState(
    val taskName: TextFieldState = TextFieldState(),
    val taskDescription: TextFieldState = TextFieldState(),
    val category: Category? = null,
    val isTaskDone: Boolean = false,
) {
    fun toTask(): Task {
        return Task(
            id = UUID.randomUUID().toString(),
            title = taskName.text.toString(),
            description = taskDescription.text.toString(),
            category = category ?: Category.OTHER,
            isCompleted = isTaskDone
        )
    }
}
