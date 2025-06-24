package dev.yovany.todoapp.navigation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.yovany.todoapp.MainActivity
import dev.yovany.todoapp.data.TaskDao
import dev.yovany.todoapp.data.TaskEntity
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.presentation.detail.TaskScreenState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class NavigationTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var taskDao: TaskDao

    // HomeScreen
    private val addTaskFabTag = "Add Task"
    private val homeScreenTag = "Home Screen"
    private val contentMessageTag = "Content Message"
    private val checkBoxTaskTag = "CheckBox Task"
    private val deleteTaskButtonTag = "Delete Button Task"

    // TaskScreen
    private val taskScreenTag = "Task Screen"
    private val taskNameInputTag = "Task Name"
    private val taskDescriptionInputTag = "Task Description"
    private val taskToggleTag = "Task Done"
    private val categoryDropdownActivatorTag = "Select Category"
    private val saveTaskButtonTag = "Save Task"

    @Before
    fun setup() {
        hiltRule.inject()

        runBlocking {
            taskDao.deleteAllTasks()
        }
    }

    @Test
    fun createNewTask_entersDataAndSaves_taskAppearsOnHomeScreen() {
        //ARRANGE
        val title = "New Task Title"
        val description = "New Task Description"
        val category = Category.WORK

        composeTestRule.onNodeWithContentDescription(addTaskFabTag)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntilAtLeastOneExists(
            hasContentDescription(taskScreenTag),
            timeoutMillis = 2_000L
        )

        composeTestRule.onNodeWithContentDescription(taskNameInputTag)
            .assertIsDisplayed()
            .performTextInput(title)

        composeTestRule.onNodeWithContentDescription(taskDescriptionInputTag)
            .assertIsDisplayed()
            .performTextInput(description)

        composeTestRule.onNodeWithContentDescription(categoryDropdownActivatorTag)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithContentDescription(category.toString())
            .assertIsDisplayed()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithContentDescription(taskToggleTag)
            .assertIsDisplayed()
            .performClick()

        //ACT
        composeTestRule.onNodeWithContentDescription(saveTaskButtonTag)
            .assertIsDisplayed()
            .performClick()

        //ASSERT
        composeTestRule.waitUntilAtLeastOneExists(
            hasContentDescription(homeScreenTag),
            timeoutMillis = 2_000L
        )

        composeTestRule.onNodeWithText(title)
            .assertIsDisplayed()
            .performScrollTo()
    }

    @Test
    fun editTask_fromHomeScreen_updatesData_reflectsOnHomeScreenAndDatabase() {
        //ARRANGE
        val title = "New Task Title"
        val description = "New Task Description"
        val category = Category.PERSONAL
        val editedTitle = "Edited Task Title"
        val editedDescription = "Edited Task Description"
        val editedCategory = Category.PERSONAL

        runBlocking {
            val task = TaskScreenState(
                taskName = title,
                taskDescription = description,
                category = category,
                isTaskDone = false
            ).toTask()

            taskDao.upsertTask(
                TaskEntity.fromTask(task)
            )
        }

        composeTestRule.onNodeWithText(title)
            .assertIsDisplayed()
            .performScrollTo()
            .performClick()

        composeTestRule.waitUntilAtLeastOneExists(
            hasContentDescription(taskScreenTag),
            timeoutMillis = 2_000L
        )

        composeTestRule.onNodeWithContentDescription(taskNameInputTag)
            .assertIsDisplayed()
            .performTextReplacement(editedTitle)

        composeTestRule.onNodeWithContentDescription(taskDescriptionInputTag)
            .assertIsDisplayed()
            .performTextReplacement(editedDescription)

        composeTestRule.onNodeWithContentDescription(categoryDropdownActivatorTag)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithContentDescription(editedCategory.toString())
            .assertIsDisplayed()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithContentDescription(taskToggleTag)
            .assertIsDisplayed()
            .performClick()

        //ACT
        composeTestRule.onNodeWithContentDescription(saveTaskButtonTag)
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        //ASSERT
        composeTestRule.waitUntilAtLeastOneExists(
            hasContentDescription(homeScreenTag),
            timeoutMillis = 2_000L
        )

        composeTestRule.onNodeWithText(editedTitle)
            .assertIsDisplayed()
            .performScrollTo()

        runBlocking {
            val tasks = taskDao.getAllTasks().first()
            Truth.assertThat(tasks).hasSize(1)
            Truth.assertThat(tasks.first().title).isEqualTo(editedTitle)
            Truth.assertThat(tasks.first().description).isEqualTo(editedDescription)
            Truth.assertThat(tasks.first().category).isEqualTo(editedCategory.ordinal)
        }

    }

    @Test
    fun toggleTaskCompletionOnHomeScreen_movesTaskBetweenSections_updatesDatabase() {
        //ARRANGE
        val title = "New Task Title"
        val description = "New Task Description"
        val category = Category.PERSONAL

        val taskState = TaskScreenState(
            taskName = title,
            taskDescription = description,
            category = category,
            isTaskDone = false
        )
        val task = taskState.toTask()

        runBlocking {
            taskDao.upsertTask(
                TaskEntity.fromTask(task)
            )
        }

        composeTestRule.onNodeWithText(title)
            .assertIsDisplayed()
            .performScrollTo()

        //ACT
        composeTestRule.onNodeWithContentDescription("$checkBoxTaskTag - ${task.id}")
            .assertIsDisplayed()
            .performClick()

        //ASSERT
        composeTestRule.onNodeWithText("0 incomplete, 1 completed")
            .assertIsDisplayed()

        runBlocking {
            val tasks = taskDao.getAllTasks().first()
            Truth.assertThat(tasks).hasSize(1)
            Truth.assertThat(tasks.first().isCompleted).isTrue()
        }
    }

    @Test
    fun deleteTaskFromHomeScreen_removesTaskFromUiAndDatabase() {
        //ARRANGE
        val title = "New Task Title"
        val description = "New Task Description"
        val category = Category.PERSONAL

        val taskState = TaskScreenState(
            taskName = title,
            taskDescription = description,
            category = category,
            isTaskDone = false)

        val task = taskState.toTask()

        runBlocking {
            taskDao.upsertTask(
                TaskEntity.fromTask(task)
            )
        }

        composeTestRule.onNodeWithText(title)
            .assertIsDisplayed()
            .performScrollTo()

        //ACT
        composeTestRule.onNodeWithContentDescription("$deleteTaskButtonTag - ${task.id}")
            .assertIsDisplayed()
            .performClick()

        //ASSERT
        composeTestRule.onNodeWithText(title)
            .assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription(contentMessageTag)
            .assertIsDisplayed()

        runBlocking {
            val tasks = taskDao.getAllTasks().first()
            Truth.assertThat(tasks).isEmpty()
        }
    }
}