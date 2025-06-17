package dev.yovany.todoapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.navigation.TaskScreenDestination
import dev.yovany.todoapp.presentation.FakeTaskLocalDataSource
import dev.yovany.todoapp.util.MainCoroutineRule
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskScreenViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeTaskLocalDataSource: FakeTaskLocalDataSource
    private lateinit var viewModel: TaskScreenViewModel

    @Before
    fun setup() {
        fakeTaskLocalDataSource = FakeTaskLocalDataSource()
        mockkStatic("androidx.navigation.SavedStateHandleKt")

        every { any<SavedStateHandle>().toRoute<TaskScreenDestination>() } answers {
            val handle = invocation.args[0] as SavedStateHandle
            val id: String? = handle["taskId"]
            TaskScreenDestination(taskId = id)
        }
    }

    @After
    fun tearDownMocks() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    private fun setupViewModelWithTaskId(taskId: String?) {
        val handle = SavedStateHandle().apply { if (taskId != null) set("taskId", taskId) }
        viewModel = TaskScreenViewModel(handle, fakeTaskLocalDataSource)
    }

    @Test
    fun `init - taskId is null - sets initial state with empty values`() = runTest {
        //ARRANGE
        setupViewModelWithTaskId(null)
        val expectedState = TaskScreenState()

        //ASSERT
        viewModel.state.test {
            val initialState = awaitItem()
            Truth.assertThat(initialState.taskName).isEqualTo(expectedState.taskName)
            Truth.assertThat(initialState.taskDescription).isEqualTo(expectedState.taskDescription)
            Truth.assertThat(initialState.category).isEqualTo(expectedState.category)
            Truth.assertThat(initialState.isTaskDone).isEqualTo(expectedState.isTaskDone)
            Truth.assertThat(initialState.canSaveTask).isEqualTo(expectedState.canSaveTask)
        }
    }

    @Test
    fun `init - taskId is valid - loads existing task and updates state`() = runTest() {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        val task = fakeTaskLocalDataSource.getCurrentTasksSnapshot().first { !it.isCompleted }
        setupViewModelWithTaskId(task.id)

        //ASSERT
        Truth.assertThat(viewModel.taskData.taskId).isEqualTo(task.id)

        viewModel.state.test {
            val loadedState = awaitItem()

            Truth.assertThat(loadedState.taskName).isEqualTo(task.title)
            Truth.assertThat(loadedState.taskDescription).isEqualTo(task.description)
            Truth.assertThat(loadedState.category).isEqualTo(task.category)
            Truth.assertThat(loadedState.isTaskDone).isEqualTo(task.isCompleted)
            Truth.assertThat(loadedState.canSaveTask).isTrue()
        }
    }

    @Test
    fun `onTaskNameChange - updates taskName in state`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        setupViewModelWithTaskId(null)

        //ACT
        viewModel.onAction(TaskScreenAction.ChangeTaskName("Task 2"))

        //ASSERT
        viewModel.state.test {
            val state = awaitItem()
            Truth.assertThat(state.taskName).isEqualTo("Task 2")
        }
    }

    @Test
    fun `onTaskDescriptionChange - updates taskDescription in state`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        setupViewModelWithTaskId(null)

        //ACT
        viewModel.onAction(TaskScreenAction.ChangeTaskDescription("Description 2"))

        //ASSERT
        viewModel.state.test {
            val state = awaitItem()
            Truth.assertThat(state.taskDescription).isEqualTo("Description 2")
        }
    }

    @Test
    fun `onCategoryChange - updates category in state`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        val task = fakeTaskLocalDataSource.getCurrentTasksSnapshot().first { !it.isCompleted }
        setupViewModelWithTaskId(task.id)

        //ACT
        viewModel.onAction(TaskScreenAction.ChangeTaskCategory(Category.WORK))

        //ASSERT
        viewModel.state.test {
            val state = awaitItem()
            Truth.assertThat(state.category).isEqualTo(Category.WORK)
        }
    }

    @Test
    fun `onTaskDoneChange - updates isTaskDone in state`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        setupViewModelWithTaskId(null)

        //ACT
        viewModel.onAction(TaskScreenAction.ChangeTaskDone(true))

        //ASSERT
        viewModel.state.test {
            val state = awaitItem()
            Truth.assertThat(state.isTaskDone).isTrue()
        }
    }

    @Test
    fun `onSaveTask - when taskId is null - creates a new task`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        setupViewModelWithTaskId(null)

        //ACT
        viewModel.onAction(TaskScreenAction.ChangeTaskName("Task 3"))
        viewModel.onAction(TaskScreenAction.ChangeTaskDescription("Description 3"))
        viewModel.onAction(TaskScreenAction.ChangeTaskCategory(Category.WORK))
        viewModel.onAction(TaskScreenAction.ChangeTaskDone(false))

        viewModel.onAction(TaskScreenAction.SaveTask)

        //ASSERT
        viewModel.event.test {
            val event = awaitItem()

            val lastTask = fakeTaskLocalDataSource.getCurrentTasksSnapshot().last()

            Truth.assertThat(event).isInstanceOf(TaskScreenEvent.TaskCreated::class.java)
            Truth.assertThat(fakeTaskLocalDataSource.getCurrentTasksSnapshot().size).isEqualTo(3)
            Truth.assertThat(lastTask.title).isEqualTo("Task 3")
            Truth.assertThat(lastTask.description).isEqualTo("Description 3")
            Truth.assertThat(lastTask.category).isEqualTo(Category.WORK)
            Truth.assertThat(lastTask.isCompleted).isFalse()
        }
    }

    @Test
    fun `onSaveTask - when taskId is not null - updates existing task`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        val task = fakeTaskLocalDataSource.getCurrentTasksSnapshot().first { it.isCompleted }
        setupViewModelWithTaskId(task.id)

        //ACT
        viewModel.onAction(TaskScreenAction.ChangeTaskName("Task 3"))
        viewModel.onAction(TaskScreenAction.ChangeTaskDescription("Description 3"))
        viewModel.onAction(TaskScreenAction.ChangeTaskCategory(Category.WORK))
        viewModel.onAction(TaskScreenAction.ChangeTaskDone(false))

        viewModel.onAction(TaskScreenAction.SaveTask)

        //ASSERT
        viewModel.event.test {
            val event = awaitItem()

            val lastTask = fakeTaskLocalDataSource.getCurrentTasksSnapshot().last()

            Truth.assertThat(event).isInstanceOf(TaskScreenEvent.TaskUpdated::class.java)
            Truth.assertThat(fakeTaskLocalDataSource.getCurrentTasksSnapshot().size).isEqualTo(2)
            Truth.assertThat(lastTask.title).isEqualTo("Task 3")
            Truth.assertThat(lastTask.description).isEqualTo("Description 3")
            Truth.assertThat(lastTask.category).isEqualTo(Category.WORK)
            Truth.assertThat(lastTask.isCompleted).isFalse()
        }


    }
}