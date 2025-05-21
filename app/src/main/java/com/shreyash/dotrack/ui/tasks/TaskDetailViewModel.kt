package com.shreyash.dotrack.ui.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.usecase.task.CompleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.DeleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTaskByIdUseCase
import com.shreyash.dotrack.domain.usecase.task.UncompleteTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val uncompleteTaskUseCase: UncompleteTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _task = MutableStateFlow<Result<Task>>(Result.Loading)
    val task: StateFlow<Result<Task>> = _task.asStateFlow()

    private var taskId: String? = null

    var deleteResult by mutableStateOf<Result<Unit>?>(null)
        private set

    fun loadTask(id: String) {
        taskId = id
        viewModelScope.launch {
            getTaskByIdUseCase(id).collectLatest { result ->
                _task.value = result
            }
        }
    }

    fun completeTask() {
        taskId?.let { id ->
            viewModelScope.launch {
                completeTaskUseCase(id)
            }
        }
    }

    fun uncompleteTask() {
        taskId?.let { id ->
            viewModelScope.launch {
                uncompleteTaskUseCase(id)
            }
        }
    }

    fun deleteTask() {
        taskId?.let { id ->
            viewModelScope.launch {
                val result = deleteTaskUseCase(id)
                deleteResult = result
            }
        }
    }
}