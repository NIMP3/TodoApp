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

}