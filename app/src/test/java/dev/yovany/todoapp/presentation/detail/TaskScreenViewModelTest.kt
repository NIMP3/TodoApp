package dev.yovany.todoapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.google.common.truth.Truth
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
            Truth.assertThat(initialState.taskName.text.toString()).isEqualTo(expectedState.taskName.text.toString())
            Truth.assertThat(initialState.taskDescription.text.toString()).isEqualTo(expectedState.taskDescription.text.toString())
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

            Truth.assertThat(loadedState.taskName.text.toString()).isEqualTo(task.title)
            Truth.assertThat(loadedState.taskDescription.text.toString()).isEqualTo(task.description)
            Truth.assertThat(loadedState.category).isEqualTo(task.category)
            Truth.assertThat(loadedState.isTaskDone).isEqualTo(task.isCompleted)
            Truth.assertThat(loadedState.canSaveTask).isTrue()
        }
    }
}