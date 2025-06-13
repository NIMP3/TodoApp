package dev.yovany.todoapp.presentation.home

import app.cash.turbine.test
import dev.yovany.todoapp.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.google.common.truth.Truth
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.presentation.FakeTaskLocalDataSource

@ExperimentalCoroutinesApi
class HomeScreenViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeTaskLocalDataSource: FakeTaskLocalDataSource
    private lateinit var viewModel: HomeScreenViewModel

    @Before
    fun setup() {
        fakeTaskLocalDataSource = FakeTaskLocalDataSource()
        viewModel = HomeScreenViewModel(fakeTaskLocalDataSource)
    }

    @Test
    fun `init - sets initial date and observers taskFlow with initial empty tasks`() = runTest {
        //ARRANGE
        val expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EE, MMMM, dd, yyyy"))

        //ASSERT
        viewModel.state.test {
            val initialState = awaitItem()

            Truth.assertThat(initialState.date).isEqualTo(expectedDate)
            Truth.assertThat(initialState.summary).isEqualTo("0 incomplete, 0 completed")
            Truth.assertThat(initialState.completedTasks).isEmpty()
            Truth.assertThat(initialState.pendingTasks).isEmpty()
        }
    }

    @Test
    fun `taskFlow emission - updates summary, completedTasks and pendingTasks correctly when tasks are emitted`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()

        //ASSERT
        viewModel.state.test {
            val initialState = awaitItem()

            Truth.assertThat(initialState.summary).isEqualTo("1 incomplete, 1 completed")
            Truth.assertThat(initialState.completedTasks).hasSize(1)
            Truth.assertThat(initialState.pendingTasks).hasSize(1)
        }
    }

    @Test
    fun `onAction OnDeletedAllTasks - clears all tasks in the local data source and sends DeleteAllTasks event`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()

        //ACT
        viewModel.onAction(HomeScreenAction.OnDeleteAllTasks)

        //ASSERT
        viewModel.event.test {
            val event = awaitItem()

            Truth.assertThat(event).isInstanceOf(HomeScreenEvent.DeletedAllTasks::class.java)
            Truth.assertThat(fakeTaskLocalDataSource.getCurrentTasksSnapshot()).isEmpty()
        }

        viewModel.state.test {
            val state = awaitItem()

            Truth.assertThat(state.summary).isEqualTo("0 incomplete, 0 completed")
            Truth.assertThat(state.completedTasks).isEmpty()
            Truth.assertThat(state.pendingTasks).isEmpty()
        }
    }

    @Test
    fun `onAction OnDeleteTask - removes especific task from the local data source and sends DeleteTask event`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        val taskToDelete = fakeTaskLocalDataSource.getCurrentTasksSnapshot().first { it.isCompleted }

        //ACT
        viewModel.onAction(HomeScreenAction.OnDeleteTask(taskToDelete))

        //ASSERT
        viewModel.event.test {
            val event = awaitItem()
            Truth.assertThat(event).isInstanceOf(HomeScreenEvent.DeletedTask::class.java)
            Truth.assertThat(fakeTaskLocalDataSource.getCurrentTasksSnapshot()).hasSize(1)
            Truth.assertThat(fakeTaskLocalDataSource.getCurrentTasksSnapshot().first()).isNotEqualTo(taskToDelete)
        }

        viewModel.state.test {
            val state = awaitItem()
            Truth.assertThat(state.summary).isEqualTo("1 incomplete, 0 completed")
            Truth.assertThat(state.completedTasks).isEmpty()
            Truth.assertThat(state.pendingTasks).hasSize(1)
        }
    }

    @Test
    fun `onAction OnToggleTask - updates task to completed in the local data source and sends UpdatedTasks event`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        val incompleteTask = fakeTaskLocalDataSource.getCurrentTasksSnapshot().first { !it.isCompleted }
        val incompleteTaskPosition = fakeTaskLocalDataSource.getCurrentTasksSnapshot().indexOf(incompleteTask)

        //ACT
        viewModel.onAction(HomeScreenAction.OnToggleTask(incompleteTask))

        //ASSERT
        viewModel.event.test {
            val event = awaitItem()

            Truth.assertThat(event).isInstanceOf(HomeScreenEvent.UpdatedTasks::class.java)
            Truth.assertThat(fakeTaskLocalDataSource.getCurrentTasksSnapshot()[incompleteTaskPosition].isCompleted).isTrue()
        }

        viewModel.state.test {
            val state = awaitItem()

            Truth.assertThat(state.summary).isEqualTo("0 incomplete, 2 completed")
            Truth.assertThat(state.completedTasks).hasSize(2)
            Truth.assertThat(state.pendingTasks).isEmpty()
        }
    }

    @Test
    fun `onAction OnToggleTask - updates task to incomplete in the local data source and sends UpdatedTasks event`() = runTest {
        //ARRANGE
        fakeTaskLocalDataSource.loadTestTasks()
        val completedTask = fakeTaskLocalDataSource.getCurrentTasksSnapshot().first { it.isCompleted }
        val completedTaskPosition = fakeTaskLocalDataSource.getCurrentTasksSnapshot().indexOf(completedTask)

        //ACT
        viewModel.onAction(HomeScreenAction.OnToggleTask(completedTask))

        //ASSERT
        viewModel.event.test {
            val event = awaitItem()

            Truth.assertThat(event).isInstanceOf(HomeScreenEvent.UpdatedTasks::class.java)
            Truth.assertThat(fakeTaskLocalDataSource.getCurrentTasksSnapshot()[completedTaskPosition].isCompleted).isFalse()
        }

        viewModel.state.test {
            val state = awaitItem()

            Truth.assertThat(state.summary).isEqualTo("2 incomplete, 0 completed")
            Truth.assertThat(state.completedTasks).isEmpty()
            Truth.assertThat(state.pendingTasks).hasSize(2)
        }

    }
}