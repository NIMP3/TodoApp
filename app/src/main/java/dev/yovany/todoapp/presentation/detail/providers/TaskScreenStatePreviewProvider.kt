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
                taskName = "I have to do my homework",
                taskDescription = "I have to do my homework for the next week",
                isTaskDone = false,
                categories = listOf(STUDY, PERSONAL),
            ),
            TaskScreenState(
                taskName = "I have to do exercise for the next week",
                taskDescription = "I have to do exercise for the next week",
                isTaskDone = true,
                categories = listOf(STUDY, PERSONAL),
            ),
            TaskScreenState(
                taskName = "I have to prepare the presentation for the next week",
                taskDescription = "I have to prepare the presentation for the next week",
                isTaskDone = false,
                categories = listOf(WORK, PERSONAL),
            ),
            TaskScreenState(
                taskName = "I have to buy a new phone",
                taskDescription = "I have to buy a new phone",
                isTaskDone = true,
                categories = listOf(SHOPPING, PERSONAL),
            ),
            TaskScreenState(
                taskName = "I have to apply for my car license",
                taskDescription = "I have to apply for my car license",
                isTaskDone = false,
                categories = listOf(
                    WORK,
                    PERSONAL,
                    SHOPPING
                )
            )
        )
}