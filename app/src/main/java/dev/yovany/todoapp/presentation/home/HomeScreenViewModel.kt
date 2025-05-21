package dev.yovany.todoapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.yovany.todoapp.data.FakeTaskLocalDataSource
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

class HomeScreenViewModel: ViewModel() {
    private val taskLocalDataSource = FakeTaskLocalDataSource()

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
            val completedTasks = it.filter { task -> task.isCompleted }
            val pendingTasks = it.filter { task -> !task.isCompleted }

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
}