package dev.yovany.todoapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.yovany.todoapp.presentation.FakeTaskLocalDataSource
import dev.yovany.todoapp.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
        viewModel = TaskScreenViewModel(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "taskId" to null
                )
            ),
            taskLocalDataSource = fakeTaskLocalDataSource
        )
    }

    @Test
    fun `init - sets initial state`() = runTest {
        //ARRANGE
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
}