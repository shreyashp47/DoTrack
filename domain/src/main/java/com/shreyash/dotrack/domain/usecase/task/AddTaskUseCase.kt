package com.shreyash.dotrack.domain.usecase.task

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.repository.TaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        dueDate: LocalDateTime? = null,
        priority: Priority,
        reminderEnabled: Boolean
    ): Result<Unit> {
        if (title.isBlank()) {
            return Result.Error(Exception("Title cannot be empty"))
        }

        return taskRepository.addTask(
            title = title,
            description = description,
            dueDate = dueDate,
            priority = priority,
            reminderEnabled = reminderEnabled
        )
    }
}