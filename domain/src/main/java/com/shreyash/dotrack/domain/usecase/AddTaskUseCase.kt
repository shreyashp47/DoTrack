package com.shreyash.dotrack.domain.usecase

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
        dueDate: LocalDateTime,
        priority: Priority
    ): Result<Unit> {
        if (title.isBlank()) {
            return Result.error(IllegalArgumentException("Title cannot be empty"))
        }

        return taskRepository.addTask(
            title = title,
            description = description,
            dueDate = dueDate,
            priority = priority
        )
    }
}