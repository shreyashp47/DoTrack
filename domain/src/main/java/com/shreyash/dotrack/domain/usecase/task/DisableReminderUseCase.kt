package com.shreyash.dotrack.domain.usecase.task

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Use case for disabling reminders for a task
 */
class DisableReminderUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * Disable reminder for a task
     * @param taskId The ID of the task
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(taskId: String): Result<Unit> {
        return taskRepository.disableReminder(taskId)
    }
}