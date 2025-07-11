package com.shreyash.dotrack.ui.tasks

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.core.util.WallpaperGenerator
import com.shreyash.dotrack.domain.ReminderScheduler
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.usecase.preferences.GetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.task.CompleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.DeleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.DisableReminderUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTasksUseCase
import com.shreyash.dotrack.domain.usecase.task.UncompleteTaskUseCase
import com.shreyash.dotrack.widget.TaskWidgetUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getTasksUseCase: GetTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val uncompleteTaskUseCase: UncompleteTaskUseCase,
    private val wallpaperGenerator: WallpaperGenerator,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getAutoWallpaperEnabledUseCase: GetAutoWallpaperEnabledUseCase,
    private val disableReminderUseCase: DisableReminderUseCase,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val TAG = "TasksViewModel"
    val tasks: StateFlow<Result<List<Task>>> = getTasksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    var wallpaperUpdated by mutableStateOf(false)
        private set

    fun completeTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Complete the task
            val result = completeTaskUseCase(id)
            if (result.isSuccess()) {
                // Disable the reminder for the completed task
                disableReminderUseCase(id)
                
                // Check if auto wallpaper is enabled
                val autoWallpaperEnabled = getAutoWallpaperEnabledUseCase().first()
                if (autoWallpaperEnabled) {
                    updateWallpaper()
                }

                reminderScheduler.cancelReminder(id)
                
                // Update widgets immediately
                withContext(Dispatchers.Main) {
                    TaskWidgetUpdater.updateTaskWidgets(context)
                }
            }
        }
    }

    fun uncompleteTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = uncompleteTaskUseCase(id)
            if (result.isSuccess()) {
                // Check if auto wallpaper is enabled
                val autoWallpaperEnabled = getAutoWallpaperEnabledUseCase().first()
                if (autoWallpaperEnabled) {
                    updateWallpaper()
                }
                
                // Update widgets immediately
                withContext(Dispatchers.Main) {
                    TaskWidgetUpdater.updateTaskWidgets(context)
                }
            }
        }
    }

    /**
     * Update the wallpaper with the latest task list
     */
    fun updateWallpaper() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Updating wallpaper...")
            val tasksResult = getTasksUseCase().first()

            if (tasksResult.isSuccess()) {
                val tasks = tasksResult.getOrNull() ?: emptyList()
                val wallpaperResult = wallpaperGenerator.generateAndSetWallpaper(tasks)
                // Update UI state on the main thread
                withContext(Dispatchers.Main) {
                    wallpaperUpdated = wallpaperResult.isSuccess()
                }
            }
        }
    }

    /**
     * Delete a specific task by ID
     */
    fun deleteTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteTaskUseCase(id)
            if (result.isSuccess()) {
                // Check if auto wallpaper is enabled
                val autoWallpaperEnabled = getAutoWallpaperEnabledUseCase().first()
                if (autoWallpaperEnabled) {
                    updateWallpaper()
                }
                
                // Update widgets immediately
                withContext(Dispatchers.Main) {
                    TaskWidgetUpdater.updateTaskWidgets(context)
                }
            }
        }
    }

    /**
     * Delete all tasks
     */
    fun deleteAllTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteTaskUseCase()
            if (result.isSuccess()) {
                // Check if auto wallpaper is enabled
                val autoWallpaperEnabled = getAutoWallpaperEnabledUseCase().first()
                if (autoWallpaperEnabled) {
                    updateWallpaper()
                }
                
                // Update widgets immediately
                withContext(Dispatchers.Main) {
                    TaskWidgetUpdater.updateTaskWidgets(context)
                }
            }
        }
    }
}