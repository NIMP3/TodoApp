package dev.yovany.todoapp.presentation.home.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.presentation.home.HomeDataState

class HomeScreenPreviewProvider: PreviewParameterProvider<HomeDataState> {
    override val values: Sequence<HomeDataState>
        get() = sequenceOf(
            HomeDataState(
                date = "May 20, 2025",
                summary = "12, incomplete, 2, completed",
                completedTasks = completedTasks,
                pendingTasks = pendingTasks,
            )
        )

}

val completedTasks = mutableListOf(
    Task(
        id = "1",
        title = "I have to do my homework",
        description = "I have to do my homework for the next week",
        isCompleted = true,
        category = Category.STUDY
    ),
    Task(
        id = "2",
        title = "I have to do exercise for the next week",
        description = "I have to do exercise for the next week",
        isCompleted = true,
        category = Category.PERSONAL
    ))

val pendingTasks = mutableListOf(
    Task(
        id = "5",
        title = "I have to prepare the presentation for the next week",
        description = "I have to prepare the presentation for the next week",
        isCompleted = false,
        category = Category.WORK
    ),
    Task(
        id = "6",
        title = "I have to buy a new phone for the next week",
        description = "I have to buy a new phone for the next week",
        isCompleted = false,
        category = Category.SHOPPING
    ),
    Task(
        id = "7",
        title = "I have to ride my bike and go out with my father",
        description = "I have to ride my bike and go out with my father",
        isCompleted = false,
        category = Category.PERSONAL
    ),
    Task(
        id = "8",
        title = "I have to manage my PBIs on the Azure DevOps",
        description = "I have to manage my PBIs on the Azure DevOps",
        isCompleted = false,
        category = Category.WORK
    ),
    Task(
        id = "9",
        title = "I have to watch a movie with my family",
        description = "I have to watch a movie with my family",
        isCompleted = false,
        category = Category.PERSONAL
    ),
    Task(
        id = "10",
        title = "I have to pay the bills",
        description = "I have to pay the bills",
        isCompleted = false,
        category = Category.SHOPPING
    ),
    Task(
        id = "11",
        title = "I have to go to the supermarket",
        description = "I have to go to the supermarket",
        isCompleted = false,
        category = Category.PERSONAL
    ),
    Task(
        id = "12",
        title = "I have to apply for my car license",
        description = "I have to apply for my car license",
        isCompleted = false,
        category = Category.OTHER
    ),
    Task(
        id = "13",
        title = "I have to go to the gym",
        description = "I have to go to the gym",
        isCompleted = false,
        category = Category.PERSONAL
    ),
    Task(
        id = "14",
        title = "I have to go to the bank",
        description = "I have to go to the bank",
        isCompleted = false,
        category = Category.OTHER
    ),
    Task(
        id = "15",
        title = "I have to go to the park",
        description = "I have to go to the park",
        isCompleted = false,
        category = Category.PERSONAL
    ),
    Task(
        id = "16",
        title = "I have to go to the beach",
        description = "I have to go to the beach",
        isCompleted = false,
        category = Category.PERSONAL
    ))