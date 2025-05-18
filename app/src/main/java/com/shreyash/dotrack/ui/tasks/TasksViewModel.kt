package com.shreyash.dotrack.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.usecase.task.CompleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTasksUseCase
import com.shreyash.dotrack.domain.usecase.task.UncompleteTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    getTasksUseCase: GetTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val uncompleteTaskUseCase: UncompleteTaskUseCase
) : ViewModel() {
    
    val tasks: StateFlow<Result<List<Task>>> = getTasksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )
    
    fun completeTask(id: String) {
        viewModelScope.launch {
            completeTaskUseCase(id)
        }
    }
    
    fun uncompleteTask(id: String) {
        viewModelScope.launch {
            uncompleteTaskUseCase(id)
        }
    }
}