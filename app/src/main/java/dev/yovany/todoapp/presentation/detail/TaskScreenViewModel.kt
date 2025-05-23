package dev.yovany.todoapp.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.yovany.todoapp.data.FakeTaskLocalDataSource
import dev.yovany.todoapp.presentation.detail.TaskScreenAction.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TaskScreenViewModel: ViewModel() {
    private val taskLocalDataSource = FakeTaskLocalDataSource()

    private var _state: MutableStateFlow<TaskScreenState> = MutableStateFlow(TaskScreenState())
    val state: StateFlow<TaskScreenState> = _state

    private val eventChannel = Channel<TaskScreenEvent>()
    val event = eventChannel.receiveAsFlow()

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
                    val task = state.value.toTask()
                    taskLocalDataSource.addTask(task)
                    eventChannel.send(TaskScreenEvent.TaskCreated)
                }
                else -> Unit
            }
        }
    }
}