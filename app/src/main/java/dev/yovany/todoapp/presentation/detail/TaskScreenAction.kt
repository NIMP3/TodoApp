package dev.yovany.todoapp.presentation.detail

import dev.yovany.todoapp.domain.Category

sealed interface TaskScreenAction {
    data object SaveTask : TaskScreenAction
    data object Back : TaskScreenAction
    data class ChangeTaskCategory(val category: Category?) : TaskScreenAction
    data class ChangeTaskDone(val isTaskDone: Boolean) : TaskScreenAction
}