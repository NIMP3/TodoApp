package dev.yovany.todoapp.presentation.detail.providers

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.yovany.todoapp.domain.Category.OTHER
import dev.yovany.todoapp.domain.Category.PERSONAL
import dev.yovany.todoapp.domain.Category.SHOPPING
import dev.yovany.todoapp.domain.Category.STUDY
import dev.yovany.todoapp.domain.Category.WORK
import dev.yovany.todoapp.presentation.detail.TaskScreenState

class TaskScreenStatePreviewProvider: PreviewParameterProvider<TaskScreenState> {
    override val values: Sequence<TaskScreenState>
        get() = sequenceOf(
            TaskScreenState(
                taskName = TextFieldState("I have to do my homework"),
                taskDescription = TextFieldState("I have to do my homework for the next week"),
                isTaskDone = false,
                category = STUDY
            ),
            TaskScreenState(
                taskName = TextFieldState("I have to do exercise for the next week"),
                taskDescription = TextFieldState("I have to do exercise for the next week"),
                isTaskDone = true,
                category = PERSONAL
            ),
            TaskScreenState(
                taskName = TextFieldState("I have to prepare the presentation for the next week"),
                taskDescription = TextFieldState("I have to prepare the presentation for the next week"),
                isTaskDone = false,
                category = WORK
            ),
            TaskScreenState(
                taskName = TextFieldState("I have to buy a new phone"),
                taskDescription = TextFieldState("I have to buy a new phone"),
                isTaskDone = true,
                category = SHOPPING
            ),
            TaskScreenState(
                taskName = TextFieldState("I have to apply for my car license"),
                taskDescription = TextFieldState("I have to apply for my car license"),
                isTaskDone = false,
                category = OTHER
            )
        )
}