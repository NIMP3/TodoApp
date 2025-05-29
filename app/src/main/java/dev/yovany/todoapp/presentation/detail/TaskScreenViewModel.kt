package dev.yovany.todoapp.presentation.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.yovany.todoapp.data.FakeTaskLocalDataSource
import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.navigation.TaskScreenDestination
import dev.yovany.todoapp.presentation.detail.TaskScreenAction.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TaskScreenViewModel(saveStateHandle: SavedStateHandle): ViewModel() {
    private val fakeTaskLocalDataSource = FakeTaskLocalDataSource
    val taskData = saveStateHandle.toRoute<TaskScreenDestination>()

    private var _state: MutableStateFlow<TaskScreenState> = MutableStateFlow(TaskScreenState())
    val state: StateFlow<TaskScreenState> = _state
    private val canSaveTask = snapshotFlow { state.value.taskName.text.toString() }

    private val eventChannel = Channel<TaskScreenEvent>()
    val event = eventChannel.receiveAsFlow()

    private var editedTask: Task? = null

    init {
        taskData.taskId?.let { taskId ->
            viewModelScope.launch {
                val task = fakeTaskLocalDataSource.getTaskById(taskId)
                editedTask = task
                _state.value = _state.value.copy(
                    taskName = TextFieldState(editedTask?.title ?: ""),
                    taskDescription = TextFieldState(editedTask?.description ?: ""),
                    isTaskDone = editedTask?.isCompleted == true,
                    category = editedTask?.category
                )
            }
        }

        canSaveTask.onEach {
            _state.value = _state.value.copy(canSaveTask = it.isNotEmpty())
        }.launchIn(viewModelScope)
    }

    fun onAction(action: TaskScreenAction) {
        viewModelScope.launch {
            when (action) {
                is ChangeTaskCategory -> {
                    _state.value = state.value.copy(
                        category = action.category
                    )
                }
                is ChangeTaskDone -> {
                    _state.value = _state.value.copy(
                        isTaskDone = action.isTaskDone
                    )
                }
                SaveTask -> {
                    editedTask?.let {
                        fakeTaskLocalDataSource.updateTask(
                            it.copy(
                                id = it.id,
                                title = state.value.taskName.text.toString(),
                                description = state.value.taskDescription.text.toString(),
                                isCompleted = state.value.isTaskDone,
                                category = state.value.category
                            )
                        )
                    } ?: run {
                        val task = state.value.toTask()
                        fakeTaskLocalDataSource.addTask(task)
                        eventChannel.send(TaskScreenEvent.TaskCreated)
                    }
                }
                else -> Unit
            }
        }
    }
}