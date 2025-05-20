package com.shreyash.dotrack.ui.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.core.util.WallpaperGenerator
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.usecase.task.CompleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTasksUseCase
import com.shreyash.dotrack.domain.usecase.task.UncompleteTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val uncompleteTaskUseCase: UncompleteTaskUseCase,
    private val wallpaperGenerator: WallpaperGenerator
) : ViewModel() {
    
    val tasks: StateFlow<Result<List<Task>>> = getTasksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )
    
    var wallpaperUpdated by mutableStateOf(false)
        private set
    
    fun completeTask(id: String) {
        viewModelScope.launch {
            val result = completeTaskUseCase(id)
            if (result.isSuccess() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateWallpaper()
            }
        }
    }
    
    fun uncompleteTask(id: String) {
        viewModelScope.launch {
            val result = uncompleteTaskUseCase(id)
            if (result.isSuccess() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateWallpaper()
            }
        }
    }
    
    /**
     * Update the wallpaper with the latest task list
     */
    fun updateWallpaper() {
        viewModelScope.launch {
            val tasksResult = getTasksUseCase().first()
            
            if (tasksResult.isSuccess()) {
                val tasks = tasksResult.getOrNull() ?: emptyList()
                val wallpaperResult = wallpaperGenerator.generateAndSetWallpaper(tasks)
                wallpaperUpdated = wallpaperResult.isSuccess()
            }
        }
    }
    fun deleteTask(id: String) {

    }

    fun deleteAllTask() {

    }
}