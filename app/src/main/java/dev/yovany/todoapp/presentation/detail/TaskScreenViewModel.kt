package dev.yovany.todoapp.presentation.detail

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.domain.TaskLocalDataSource
import dev.yovany.todoapp.navigation.TaskScreenDestination
import dev.yovany.todoapp.presentation.detail.TaskScreenAction.ChangeTaskCategory
import dev.yovany.todoapp.presentation.detail.TaskScreenAction.ChangeTaskDescription
import dev.yovany.todoapp.presentation.detail.TaskScreenAction.ChangeTaskDone
import dev.yovany.todoapp.presentation.detail.TaskScreenAction.ChangeTaskName
import dev.yovany.todoapp.presentation.detail.TaskScreenAction.SaveTask
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskLocalDataSource: TaskLocalDataSource): ViewModel() {
    val taskData = savedStateHandle.toRoute<TaskScreenDestination>()

    private var _state: MutableStateFlow<TaskScreenState> = MutableStateFlow(TaskScreenState())
    val state: StateFlow<TaskScreenState> = _state
    private val canSaveTask = snapshotFlow { state.value.taskName }

    private val eventChannel = Channel<TaskScreenEvent>()
    val event = eventChannel.receiveAsFlow()

    private var editedTask: Task? = null

    init {
        Log.d("TaskScreenViewModel", "init")
        taskData.taskId?.let { taskId ->
            Log.d("TaskScreenViewModel", "init: $taskId")
            viewModelScope.launch {
                Log.d("TaskScreenViewModel", "init: launch")
                val task = taskLocalDataSource.getTaskById(taskId)
                editedTask = task
                _state.value = _state.value.copy(
                    taskName = editedTask?.title ?: "",
                    taskDescription = editedTask?.description ?: "",
                    isTaskDone = editedTask?.isCompleted == true,
                    category = editedTask?.category
                )
                Log.d("TaskScreenViewModel", "init: ${_state.value}")
            }
        } ?: Log.d("TaskScreenViewModel", "init: null")

        canSaveTask.onEach {
            _state.value = _state.value.copy(canSaveTask = it.isNotEmpty())
        }.launchIn(viewModelScope)
    }

    fun onAction(action: TaskScreenAction) {
        viewModelScope.launch {
            when (action) {
                is ChangeTaskName -> {
                    _state.value = state.value.copy(
                        taskName = action.name
                    )
                }
                is ChangeTaskDescription -> {
                    _state.value = state.value.copy(
                        taskDescription = action.description
                    )
                }
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
                        taskLocalDataSource.updateTask(
                            it.copy(
                                id = it.id,
                                title = state.value.taskName,
                                description = state.value.taskDescription,
                                isCompleted = state.value.isTaskDone,
                                category = state.value.category
                            )
                        )

                        eventChannel.send(TaskScreenEvent.TaskUpdated)
                    } ?: run {
                        val task = state.value.toTask()
                        taskLocalDataSource.addTask(task)
                        eventChannel.send(TaskScreenEvent.TaskCreated)
                    }
                }
                else -> Unit
            }
        }
    }
}