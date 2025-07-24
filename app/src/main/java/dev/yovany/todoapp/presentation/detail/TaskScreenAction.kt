package dev.yovany.todoapp.presentation.detail

import dev.yovany.todoapp.domain.Category

sealed interface TaskScreenAction {
    data object SaveTask : TaskScreenAction
    data object Back : TaskScreenAction
    data class ChangeTaskName(val name: String) : TaskScreenAction
    data class ChangeTaskDescription(val description: String?) : TaskScreenAction
    data class ChangeTaskCategories(val categories: List<Category>) : TaskScreenAction
    data class ChangeTaskDone(val isTaskDone: Boolean) : TaskScreenAction
}