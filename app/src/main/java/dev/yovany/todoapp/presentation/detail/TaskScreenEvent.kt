package dev.yovany.todoapp.presentation.detail

sealed interface TaskScreenEvent {
    data object TaskCreated : TaskScreenEvent
    data object TaskUpdated : TaskScreenEvent
}