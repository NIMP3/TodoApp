package dev.yovany.todoapp.presentatio.detail

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.AnnotatedString
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.presentation.detail.TaskScreen
import dev.yovany.todoapp.presentation.detail.TaskScreenAction
import dev.yovany.todoapp.presentation.detail.TaskScreenState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class TaskScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun taskScreen_whenNoTask_displayInitialState() {
        //ARRANGE
        val initialState = TaskScreenState()

        composeTestRule.setContent {
            TaskScreen(
                state = initialState,
                onTaskScreenAction = {}
            )
        }

        //ASSERT
        composeTestRule.onNodeWithContentDescription("Task Name")
            .assertExists()
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.EditableText,
                    AnnotatedString(initialState.taskName)
            ))

        composeTestRule.onNodeWithContentDescription("Task Description")
            .assertExists()
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.EditableText,
                    AnnotatedString(initialState.taskDescription ?: "")
            ))

        composeTestRule.onNodeWithContentDescription("Task Done")
            .assertExists()
            .assertIsOff()

        composeTestRule.onNodeWithContentDescription("Save Task")
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotEnabled()

        composeTestRule.onNodeWithContentDescription("Select Category")
            .assertExists()
    }

    @Test
    fun tasScreen_whenTask_displayTaskState() {
        //ARRANGE
        val initialState = TaskScreenState(
            taskName = "Task Name",
            taskDescription = "Task Description",
            category = Category.WORK,
            isTaskDone = true,
            canSaveTask = true
        )

        composeTestRule.setContent {
            TaskScreen(
                state = initialState,
                onTaskScreenAction = {}
            )
        }

        //ASSERT
        composeTestRule.onNodeWithContentDescription("Task Name")
            .assertExists()
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.EditableText,
                    AnnotatedString(initialState.taskName)
                )
            )

        composeTestRule.onNodeWithContentDescription("Task Description")
            .assertExists()
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.EditableText,
                    AnnotatedString(initialState.taskDescription ?: "")
                )
            )

        composeTestRule.onNodeWithContentDescription("Task Done")
            .assertExists()
            .assertIsOn()

        composeTestRule.onNodeWithContentDescription("Save Task")
            .assertExists()
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule.onNodeWithContentDescription("Selected Category")
            .assertExists()
            .assertTextEquals(initialState.category.toString())
    }

    @Test
    fun taskScreen_toggleTaskDone_invokesChangeTaskDoneActionAndCheckBoxIsChecked() {
        //ARRANGE
        val initialState = TaskScreenState()

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<TaskScreenAction>()
        val testOnAction: (TaskScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)

            when(action) {
                is TaskScreenAction.ChangeTaskDone -> {
                    assertEquals(true, action.isTaskDone)
                }
                else -> {}
            }
        }

        composeTestRule.setContent {
            TaskScreen(
                state = initialState,
                onTaskScreenAction = testOnAction
            )
        }

        val checkBoxTaskDone = composeTestRule.onNodeWithContentDescription("Task Done")
        checkBoxTaskDone.assertExists()

        //ACT
        checkBoxTaskDone.performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            TaskScreenAction.ChangeTaskDone(true),
            capturedAction.get())
    }

    @Test
    fun taskScreen_changeTaskCategory_invokesChangeTaskCategoryActionAndSelectedCategoryChanged() {
        //ARRANGE
        val initialState = TaskScreenState(
            taskName = "Task Name",
            taskDescription = "Task Description",
            category = Category.WORK,
            isTaskDone = true,
            canSaveTask = true
        )

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<TaskScreenAction>()
        val testOnAction: (TaskScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)

            when(action) {
                is TaskScreenAction.ChangeTaskCategory -> {
                    assertEquals(Category.OTHER, action.category)
                }
                else -> {}
            }
        }

        composeTestRule.setContent {
            TaskScreen(
                state = initialState,
                onTaskScreenAction = testOnAction
            )
        }

        composeTestRule.onNodeWithContentDescription("Select Category")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        //ACT
        composeTestRule.onNodeWithContentDescription(Category.OTHER.toString())
            .assertExists()
            .performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            TaskScreenAction.ChangeTaskCategory(Category.OTHER),
            capturedAction.get())
    }

    @Test
    fun taskScreen_changeTaskDescription_invokesChangeTaskDescriptionActionAndTaskDescriptionChanged() {
        //ARRANGE
        val initialState = TaskScreenState()

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<TaskScreenAction>()
        val testOnAction: (TaskScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)
            when (action) {
                is TaskScreenAction.ChangeTaskDescription -> {
                    assertEquals("New Task Description", action.description)
                }

                else -> {}
            }
        }

        composeTestRule.setContent {
            TaskScreen(
                state = initialState,
                onTaskScreenAction = testOnAction
            )
        }

        val taskDescription = composeTestRule.onNodeWithContentDescription("Task Description")
        taskDescription.assertExists()

        //ACT
        taskDescription.performTextInput("New Task Description")

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            TaskScreenAction.ChangeTaskDescription("New Task Description"),
            capturedAction.get())
    }

    @Test
    fun taskScreen_changeTaskName_invokesChangeTaskNameActionAndTaskNameChanged() {
        //ARRANGE
        val initialState = TaskScreenState()

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<TaskScreenAction>()
        val testOnAction: (TaskScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)

            when (action) {
                is TaskScreenAction.ChangeTaskName -> {
                    assertEquals("New Task Name", action.name)
                }
                else -> {}
            }
        }

        composeTestRule.setContent {
            TaskScreen(
                state = initialState,
                onTaskScreenAction = testOnAction
            )
        }

        val taskName = composeTestRule.onNodeWithContentDescription("Task Name")
        taskName.assertExists()

        //ACT
        taskName.performTextInput("New Task Name")

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            TaskScreenAction.ChangeTaskName("New Task Name"),
            capturedAction.get())
    }

    @Test
    fun taskScreen_onBack_invokesBackAction() {
        //ARRANGE
        val initialState = TaskScreenState()

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<TaskScreenAction>()
        val testOnAction: (TaskScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)
        }

        composeTestRule.setContent {
            TaskScreen(
                state = initialState,
                onTaskScreenAction = testOnAction
            )
        }

        //ACT
        composeTestRule.onNodeWithContentDescription("Back")
            .assertExists()
            .performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            TaskScreenAction.Back,
            capturedAction.get())
    }

    @Test
    fun taskScreen_onSaveTask_invokesSaveTaskAction() {
        //ARRANGE
        val initialState = TaskScreenState(canSaveTask = true)

        val onActionCalled = AtomicBoolean(false)
        val capturedAction = AtomicReference<TaskScreenAction>()
        val testOnAction: (TaskScreenAction) -> Unit = { action ->
            onActionCalled.set(true)
            capturedAction.set(action)
        }

        composeTestRule.setContent {
            TaskScreen(
                state = initialState,
                onTaskScreenAction = testOnAction
            )
        }

        //ACT
        composeTestRule.onNodeWithContentDescription("Save Task")
            .assertExists()
            .performClick()

        //ASSERT
        assertTrue("onAction lambda was not called.", onActionCalled.get())
        assertEquals(
            "The wrong action was invoked or action has unexpected parameters.",
            TaskScreenAction.SaveTask,
            capturedAction.get())
    }
}