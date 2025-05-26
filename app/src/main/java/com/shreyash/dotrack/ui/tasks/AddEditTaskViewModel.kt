package com.shreyash.dotrack.ui.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.core.util.WallpaperGenerator
import com.shreyash.dotrack.domain.ReminderScheduler
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.usecase.task.AddTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTaskByIdUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTasksUseCase
import com.shreyash.dotrack.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

data class AddEditTaskUiState constructor(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val dueDate: LocalDateTime? = null,
    val priority: Priority = Priority.MEDIUM,
    val isCompleted: Boolean = false,
    val reminderEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveResult: Result<Unit>? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val wallpaperUpdated: Boolean = false
)

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val wallpaperGenerator: WallpaperGenerator,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    var uiState by mutableStateOf(AddEditTaskUiState())
        private set

    fun loadTask(taskId: String) {
        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            getTaskByIdUseCase(taskId).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val task = result.data
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                id = task.id,
                                title = task.title,
                                description = task.description,
                                dueDate = task.dueDate,
                                priority = task.priority,
                                isCompleted = task.isCompleted,
                                createdAt = task.createdAt,
                                updatedAt = task.updatedAt,
                                reminderEnabled = task.reminderEnabled,
                                isLoading = false
                            )
                        }
                    }

                    is Result.Error -> {
                        // Handle error
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(isLoading = false)
                        }
                    }

                    is Result.Loading -> {
                        // Already set loading state
                    }
                }
            }
        }
    }

    fun updateTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun updateDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun updateDueDate(dueDate: LocalDateTime?) {
        uiState = uiState.copy(dueDate = dueDate)
    }

    fun updatePriority(priority: Priority) {
        uiState = uiState.copy(priority = priority)
    }

    fun updateReminderEnabled(enabled: Boolean) {
        uiState = uiState.copy(reminderEnabled = enabled)
    }

    fun saveTask() {
        if (uiState.title.isBlank()) {
            uiState = uiState.copy(
                saveResult = Result.Error(Exception("Title cannot be empty"))
            )
            return
        }

        uiState = uiState.copy(isSaving = true)

        viewModelScope.launch(Dispatchers.IO) {
            val result = if (uiState.id == null) {
                // Add new task
                addTaskUseCase(
                    title = uiState.title,
                    description = uiState.description,
                    dueDate = uiState.dueDate,
                    priority = uiState.priority,
                    reminderEnabled = uiState.reminderEnabled
                )
            } else {
                // Update existing task
                val now = LocalDateTime.now()
                val task = Task(
                    id = uiState.id!!,
                    title = uiState.title,
                    description = uiState.description,
                    isCompleted = uiState.isCompleted,
                    dueDate = uiState.dueDate,
                    priority = uiState.priority,
                    createdAt = uiState.createdAt,
                    reminderEnabled = uiState.reminderEnabled,
                    updatedAt = now
                )
                updateTaskUseCase(task)
            }

            if (result.isSuccess()) {
                // Update wallpaper with the latest task list
                updateWallpaper()
                if (uiState.reminderEnabled && uiState.dueDate != null) {
                    reminderScheduler.scheduleReminder(
                        taskId = uiState.id.toString(), // get the ID from result or generated in use case
                        title = uiState.title,
                        dueDate = uiState.dueDate!!
                    )
                }
            }

            // Update UI state on the main thread
            withContext(Dispatchers.Main) {
                uiState = uiState.copy(
                    isSaving = false,
                    saveResult = result
                )
            }
        }
    }

    /**
     * Update the wallpaper with the latest task list
     */
    private suspend fun updateWallpaper() {
        val tasksResult = getTasksUseCase().first()

        if (tasksResult.isSuccess()) {
            val tasks = tasksResult.getOrNull() ?: emptyList()
            val wallpaperResult = wallpaperGenerator.generateAndSetWallpaper(tasks)

            withContext(Dispatchers.Main) {
                uiState = uiState.copy(
                    wallpaperUpdated = wallpaperResult.isSuccess()
                )
            }
        }
    }
}