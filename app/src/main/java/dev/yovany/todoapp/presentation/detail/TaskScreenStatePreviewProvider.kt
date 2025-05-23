package dev.yovany.todoapp.presentation.detail

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.yovany.todoapp.domain.Category.OTHER
import dev.yovany.todoapp.domain.Category.PERSONAL
import dev.yovany.todoapp.domain.Category.SHOPPING
import dev.yovany.todoapp.domain.Category.STUDY
import dev.yovany.todoapp.domain.Category.WORK

class TaskScreenStatePreviewProvider: PreviewParameterProvider<TaskScreenState> {
    override val values: Sequence<TaskScreenState>
        get() = sequenceOf(
            TaskScreenState(
                taskName = "I have to do my homework",
                taskDescription = "I have to do my homework for the next week",
                isTaskDone = false,
                category = STUDY
            ),
            TaskScreenState(
                taskName = "I have to do exercise for the next week",
                taskDescription = "I have to do exercise for the next week",
                isTaskDone = true,
                category = PERSONAL
            ),
            TaskScreenState(
                taskName = "I have to prepare the presentation for the next week",
                taskDescription = "I have to prepare the presentation for the next week",
                isTaskDone = false,
                category = WORK
            ),
            TaskScreenState(
                taskName = "I have to buy a new phone for the next week",
                taskDescription = "I have to buy a new phone for the next week",
                isTaskDone = true,
                category = SHOPPING
            ),
            TaskScreenState(
                taskName = "I have to apply for my car license",
                taskDescription = "I have to apply for my car license",
                isTaskDone = false,
                category = OTHER
            )
        )
}