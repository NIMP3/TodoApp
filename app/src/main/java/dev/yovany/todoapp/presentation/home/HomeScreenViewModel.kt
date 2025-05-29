package dev.yovany.todoapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.yovany.todoapp.TodoApplication
import dev.yovany.todoapp.data.FakeTaskLocalDataSource
import androidx.lifecycle.SavedStateHandle
import dev.yovany.todoapp.domain.TaskLocalDataSource
import dev.yovany.todoapp.presentation.home.HomeScreenAction.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskLocalDataSource: TaskLocalDataSource
): ViewModel() {

    private val _state = MutableStateFlow(HomeDataState())
    val state: StateFlow<HomeDataState> = _state

    private val eventChannel = Channel<HomeScreenEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        _state.value = state.value.copy(
            date = LocalDate.now().let {
                DateTimeFormatter.ofPattern("EE, MMMM, dd, yyyy").format(it)
            }
        )

        taskLocalDataSource.taskFlow.onEach {
            val completedTasks = it
                .filter { task -> task.isCompleted }
                .sortedByDescending { it.date }

            val pendingTasks = it
                .filter { task -> !task.isCompleted }
                .sortedByDescending { it.date }

            _state.value = _state.value.copy(
                summary = "${pendingTasks.size} incomplete, ${completedTasks.size} completed",
                completedTasks = completedTasks,
                pendingTasks = pendingTasks
            )
        }
            .launchIn(viewModelScope)
    }

    fun onAction(action: HomeScreenAction) {
        viewModelScope.launch {
            when(action) {
                OnDeleteAllTasks -> {
                    taskLocalDataSource.removeAllTasks()
                    eventChannel.send(HomeScreenEvent.DeletedAllTasks)
                }
                is OnDeleteTask -> {
                    taskLocalDataSource.removeTask(action.task)
                    eventChannel.send(HomeScreenEvent.DeletedTask)
                }
                is OnToggleTask -> {
                    val updatedTask = action.task.copy(isCompleted = !action.task.isCompleted)
                    taskLocalDataSource.updateTask(updatedTask)
                    eventChannel.send(HomeScreenEvent.UpdatedTasks)
                }
                else -> Unit
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val dataSource = (this[APPLICATION_KEY] as TodoApplication).dataSource
                HomeScreenViewModel(
                    taskLocalDataSource = dataSource,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}