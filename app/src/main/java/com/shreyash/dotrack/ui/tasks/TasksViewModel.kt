package com.shreyash.dotrack.ui.tasks

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.core.util.WallpaperGenerator
import com.shreyash.dotrack.domain.ReminderScheduler
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.model.TaskDeleteScope
import com.shreyash.dotrack.domain.usecase.preferences.GetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetSortDirectionUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetSortOptionUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetSortDirectionUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetSortOptionUseCase
import com.shreyash.dotrack.domain.usecase.task.CompleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.DeleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.DeleteTasksUseCase
import com.shreyash.dotrack.domain.usecase.task.DisableReminderUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTasksUseCase
import com.shreyash.dotrack.domain.usecase.task.UncompleteTaskUseCase
import com.shreyash.dotrack.widget.TaskWidgetUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getTasksUseCase: GetTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val uncompleteTaskUseCase: UncompleteTaskUseCase,
    private val wallpaperGenerator: WallpaperGenerator,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val deleteTasksUseCase: DeleteTasksUseCase,
    private val getAutoWallpaperEnabledUseCase: GetAutoWallpaperEnabledUseCase,
    private val disableReminderUseCase: DisableReminderUseCase,
    private val reminderScheduler: ReminderScheduler,
    private val getSortOptionUseCase: GetSortOptionUseCase,
    private val setSortOptionUseCase: SetSortOptionUseCase,
    private val getSortDirectionUseCase: GetSortDirectionUseCase,
    private val setSortDirectionUseCase: SetSortDirectionUseCase
) : ViewModel() {

    private val TAG = "TasksViewModel"

    private val _sortOption = kotlinx.coroutines.flow.MutableStateFlow(SortOption.DUE_DATE)
    private val _sortDirection = kotlinx.coroutines.flow.MutableStateFlow(SortDirection.ASCENDING)

    val sortOption: SortOption get() = _sortOption.value
    val sortDirection: SortDirection get() = _sortDirection.value

    private var sortOptionSelectedByUser = false
    private var sortDirectionSelectedByUser = false

    init {
        viewModelScope.launch {
            getSortOptionUseCase()
                .distinctUntilChanged()
                .collect { saved ->
                    if (!sortOptionSelectedByUser) _sortOption.value = saved
                }
        }
        viewModelScope.launch {
            getSortDirectionUseCase()
                .distinctUntilChanged()
                .collect { saved ->
                    if (!sortDirectionSelectedByUser) _sortDirection.value = saved
                }
        }
    }

    private val _filterPriority = kotlinx.coroutines.flow.MutableStateFlow<Priority?>(null)
    private val _filterCompleted = kotlinx.coroutines.flow.MutableStateFlow<Boolean?>(null)

    val filterPriority: Priority? get() = _filterPriority.value
    val filterCompleted: Boolean? get() = _filterCompleted.value

    val sortedTasks: StateFlow<Result<List<Task>>> = combine(
        getTasksUseCase(),
        _sortOption,
        _sortDirection,
        _filterPriority,
        _filterCompleted
    ) { result, option, direction, priorityFilter, completedFilter ->
        when (result) {
            is Result.Success -> Result.Success(
                filterTasks(
                    sortTasks(result.data, option, direction),
                    priorityFilter,
                    completedFilter
                )
            )
            is Result.Error -> result
            is Result.Loading -> result
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading
    )

    val taskCounts: StateFlow<TaskCounts> = getTasksUseCase()
        .map { result ->
            val tasks = result.getOrNull().orEmpty()
            TaskCounts(
                completed = tasks.count { it.isCompleted },
                incomplete = tasks.count { !it.isCompleted }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskCounts()
        )

    fun setSortOption(option: SortOption) {
        sortOptionSelectedByUser = true
        _sortOption.value = option
        viewModelScope.launch {
            setSortOptionUseCase(option)
        }
    }

    fun setSortDirection(direction: SortDirection) {
        sortDirectionSelectedByUser = true
        _sortDirection.value = direction
        viewModelScope.launch {
            setSortDirectionUseCase(direction)
        }
    }

    fun toggleSortDirection() {
        sortDirectionSelectedByUser = true
        val newDirection = if (_sortDirection.value == SortDirection.ASCENDING) {
            SortDirection.DESCENDING
        } else {
            SortDirection.ASCENDING
        }
        _sortDirection.value = newDirection
        viewModelScope.launch {
            setSortDirectionUseCase(newDirection)
        }
    }

    fun setFilterPriority(priority: Priority?) {
        _filterPriority.value = priority
    }

    fun setFilterStatus(completed: Boolean?) {
        _filterCompleted.value = completed
    }

    fun resetSort() {
        sortOptionSelectedByUser = true
        sortDirectionSelectedByUser = true
        _sortOption.value = SortOption.DUE_DATE
        _sortDirection.value = SortDirection.ASCENDING
        viewModelScope.launch {
            setSortOptionUseCase(SortOption.DUE_DATE)
            setSortDirectionUseCase(SortDirection.ASCENDING)
        }
    }

    fun resetFilter() {
        _filterPriority.value = null
        _filterCompleted.value = null
    }

    private fun sortTasks(tasks: List<Task>, option: SortOption, direction: SortDirection): List<Task> {
        val sorted = when (option) {
            SortOption.DUE_DATE -> tasks.sortedBy { it.dueDate ?: LocalDateTime.MAX }
            SortOption.PRIORITY -> tasks.sortedByDescending { it.priority.value }
            SortOption.CREATED_DATE -> tasks.sortedBy { it.createdAt }
            SortOption.TITLE -> tasks.sortedBy { it.title.lowercase() }
        }
        return if (direction == SortDirection.DESCENDING) sorted.reversed() else sorted
    }

    private fun filterTasks(
        tasks: List<Task>,
        priority: Priority?,
        completed: Boolean?
    ): List<Task> {
        return tasks.filter { task ->
            val matchesPriority = priority == null || task.priority == priority
            val matchesStatus = completed == null || task.isCompleted == completed
            matchesPriority && matchesStatus
        }
    }

    var wallpaperUpdated by mutableStateOf(false)
        private set

    var isUpdatingWallpaper by mutableStateOf(false)
        private set

    var isDeleting by mutableStateOf(false)
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
            isUpdatingWallpaper = true
            val wallpaperStartTime = System.currentTimeMillis()
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

            // Ensure minimum 4-second display of the progress indicator
            val elapsed = System.currentTimeMillis() - wallpaperStartTime
            val remaining = 2000L - elapsed
            if (remaining > 0) {
                delay(remaining)
            }
            isUpdatingWallpaper = false
        }
    }

    /**
     * Delete a specific task by ID
     */
    fun deleteTask(id: String) {
        isDeleting = true
        val deleteStartTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteTaskUseCase(id)
            if (result.isSuccess()) {
                val autoWallpaperEnabled = getAutoWallpaperEnabledUseCase().first()
                if (autoWallpaperEnabled) {
                    updateWallpaper()
                }
                withContext(Dispatchers.Main) {
                    TaskWidgetUpdater.updateTaskWidgets(context)
                }
            }
            val elapsed = System.currentTimeMillis() - deleteStartTime
            val remaining = 2000L - elapsed
            if (remaining > 0) {
                delay(remaining)
            }
            withContext(Dispatchers.Main) {
                isDeleting = false
            }
        }
    }

    /**
     * Delete tasks by scope (all, completed, or incomplete)
     * Cancels pending reminders for the affected tasks
     */
    fun deleteTasks(scope: TaskDeleteScope) {
        isDeleting = true
        val deleteStartTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            val tasksResult = getTasksUseCase().first()
            if (tasksResult.isSuccess()) {
                val affectedTasks = tasksResult.getOrNull()?.filter { task ->
                    when (scope) {
                        TaskDeleteScope.ALL -> true
                        TaskDeleteScope.COMPLETED -> task.isCompleted
                        TaskDeleteScope.INCOMPLETE -> !task.isCompleted
                    }
                }.orEmpty()
                affectedTasks.forEach { task ->
                    reminderScheduler.cancelReminder(task.id)
                }
            }
            val result = deleteTasksUseCase(scope)
            if (result.isSuccess()) {
                val autoWallpaperEnabled = getAutoWallpaperEnabledUseCase().first()
                if (autoWallpaperEnabled) {
                    updateWallpaper()
                }
                withContext(Dispatchers.Main) {
                    TaskWidgetUpdater.updateTaskWidgets(context)
                }
            }
            val elapsed = System.currentTimeMillis() - deleteStartTime
            val remaining = 2000L - elapsed
            if (remaining > 0) {
                delay(remaining)
            }
            withContext(Dispatchers.Main) {
                isDeleting = false
            }
        }
    }
}

data class TaskCounts(
    val completed: Int = 0,
    val incomplete: Int = 0
) {
    val total: Int get() = completed + incomplete
}