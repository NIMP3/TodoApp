package dev.yovany.todoapp.presentation.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.ui.theme.TodoAppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_whenNoTasks_displaysContentMessage() {
        //ARRANGE
        val initialState = HomeDataState()

        composeTestRule.setContent {
            TodoAppTheme {
                HomeScreen(
                    state = initialState,
                    onAction = {}
                )
            }
        }

        //ASSERT
        composeTestRule.onNodeWithContentDescription("Content Message")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_whenThereArePendingAndCompletedTasks_displaySummaryInfoSectionsAndTasks() {
        //ARRANGE
        val date = "Fri, June, 20, 2025"
        val summary = "1 incomplete, 1 completed"
        val completedTasks = listOf(
            Task(
                id = "2",
                title = "Task 2",
                description = "Description 2",
                isCompleted = true,
                categories = listOf(Category.OTHER)
            ),
            Task(
                id = "3",
                title = "Task 3",
                description = "Description 3",
                isCompleted = true,
                categories = listOf(Category.WORK)
            ),
        )
        val pendingTasks = listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                categories = listOf(Category.WORK)
            )
        )

        val initialState = HomeDataState(
            date =  date,
            summary = summary,
            completedTasks = completedTasks,
            pendingTasks = pendingTasks
        )

        composeTestRule.setContent {
            TodoAppTheme {
                HomeScreen(
                    state = initialState,
                    onAction = {}
                )
            }
        }

        //ASSERT
        composeTestRule.onNodeWithContentDescription("Summary Info")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Completed Tasks")
            .assertExists()
            .assertIsDisplayed()

        completedTasks.forEach { task ->
            composeTestRule.onNodeWithContentDescription("${task.id} - Completed Task")
                .assertExists()
                .assertIsDisplayed()
        }

        composeTestRule.onNodeWithContentDescription("Pending Tasks")
            .assertExists()
            .assertIsDisplayed()

        pendingTasks.forEach { task ->
            composeTestRule.onNodeWithContentDescription("${task.id} - Pending Task")
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun homeScreen_clickAddTaskFAB_invokesOnActionWithOnAddTask() {
        //ARRANGE
        val initialState = HomeDataState()

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<HomeScreenAction>()
        val testOnAction: (HomeScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)
        }

        composeTestRule.setContent {
            TodoAppTheme {
                HomeScreen(
                    state = initialState,
                    onAction = testOnAction
                )
            }
        }

        val fabAddTask = composeTestRule.onNodeWithContentDescription("Add Task")

        //ACT
        fabAddTask.performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            HomeScreenAction.OnAddTask,
            capturedAction.get()
        )
    }

    @Test
    fun homeScreen_clickTaskItem_invokesOnActionWthOnClickTask() {
        //ARRANGE
        val pendingTasks = listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                categories = listOf(Category.WORK)
            )
        )

        val initialState = HomeDataState(pendingTasks = pendingTasks)

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<HomeScreenAction>()
        val testOnAction: (HomeScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)
        }

        composeTestRule.setContent {
            TodoAppTheme {
                HomeScreen(
                    state = initialState,
                    onAction = testOnAction
                )
            }
        }

        val itemToClicked =
            composeTestRule.onNodeWithContentDescription("${pendingTasks.first().id} - Pending Task")

        //ACT
        itemToClicked.performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            HomeScreenAction.OnClickTask(pendingTasks.first().id),
            capturedAction.get()
        )
    }

    @Test
    fun homeScreen_toggleTaskCompletion_invokesOnActionWithOnToggleTask() {
        //ARRANGE
        val pendingTasks = listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                categories = listOf(Category.WORK)
            )
        )

        val initialState = HomeDataState(pendingTasks = pendingTasks)

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<HomeScreenAction>()
        val testOnAction: (HomeScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)
        }

        composeTestRule.setContent {
            TodoAppTheme {
                HomeScreen(
                    state = initialState,
                    onAction = testOnAction
                )
            }
        }

        val checkBoxTaskToToggle = composeTestRule.onNodeWithContentDescription("CheckBox Task - ${pendingTasks.first().id}")
        checkBoxTaskToToggle.assertExists()

        //ACT
        checkBoxTaskToToggle.performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            HomeScreenAction.OnToggleTask(pendingTasks.first()),
            capturedAction.get()
        )
    }

    @Test
    fun homeScreen_deleteTaskCompletion_invokesOnActionWithOnDeleteTask() {
        //ARRANGE
        val pendingTask =
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                categories = listOf(Category.WORK)
            )

        val initialState = HomeDataState(pendingTasks = listOf(pendingTask))

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<HomeScreenAction>()
        val testOnAction: (HomeScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)
        }

        composeTestRule.setContent {
            TodoAppTheme {
                HomeScreen(
                    state = initialState,
                    onAction = testOnAction
                )
            }
        }

        val deleteButtonTask = composeTestRule.onNodeWithContentDescription("Delete Button Task - ${pendingTask.id}")
        deleteButtonTask.assertExists()

        //ACT
        deleteButtonTask.performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            HomeScreenAction.OnDeleteTask(pendingTask),
            capturedAction.get()
        )
    }

    @Test
    fun homeScreen_openOptionsMenu_displaysDeleteAllOption() {
        //ARRANGE
        composeTestRule.setContent {
            TodoAppTheme {
                HomeScreen(
                    state = HomeDataState(),
                    onAction = {}
                )
            }
        }

        val optionsButton = composeTestRule.onNodeWithContentDescription("Options")
        optionsButton.assertExists()

        composeTestRule.onNodeWithContentDescription("Delete All")
            .assertDoesNotExist()

        //ACT
        optionsButton.performClick()

        //ASSERT
        composeTestRule.onNodeWithContentDescription("Delete All")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_clickDeleteAllTask_invokesOnActionWithDeleteAllTasksAndClosesMenu() {
        //ARRANGE
        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<HomeScreenAction>()
        val testOnAction: (HomeScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)
        }

        composeTestRule.setContent {
            TodoAppTheme {
                HomeScreen(
                    state = HomeDataState(),
                    onAction = testOnAction
                )
            }
        }

        val optionsButton = composeTestRule.onNodeWithContentDescription("Options")
        optionsButton.assertExists()

        composeTestRule.onNodeWithContentDescription("Delete All")
            .assertDoesNotExist()

        optionsButton.performClick()

        val deleteAllButton = composeTestRule.onNodeWithContentDescription("Delete All")
        deleteAllButton.assertExists().assertIsDisplayed()

        //ACT
        deleteAllButton.performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            HomeScreenAction.OnDeleteAllTasks,
            capturedAction.get()
        )

        composeTestRule.onNodeWithContentDescription("Delete All")
            .assertDoesNotExist()
    }
}