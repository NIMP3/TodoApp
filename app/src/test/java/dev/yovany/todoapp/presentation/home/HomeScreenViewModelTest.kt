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
        fakeTaskLocalDataSource.setTasks(listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                category = Category.WORK
            ),
            Task(
                id = "2",
                title = "Task 2",
                description = "Description 2",
                isCompleted = true,
                category = Category.OTHER
            )
        ))

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
        fakeTaskLocalDataSource.setTasks(listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                category = Category.WORK
            ),
            Task(
                id = "2",
                title = "Task 2",
                description = "Description 2",
                isCompleted = true,
                category = Category.OTHER
            )
        ))

        //ACT
        viewModel.onAction(HomeScreenAction.OnDeleteAllTasks)

        //ASSERT
        viewModel.event.test {
            val event = awaitItem()
            Truth.assertThat(event).isInstanceOf(HomeScreenEvent.DeletedAllTasks::class.java)
        }
        Truth.assertThat(fakeTaskLocalDataSource.getCurrentTasksSnapshot()).isEmpty()

    }

}