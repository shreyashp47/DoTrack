package com.shreyash.dotrack.domain.usecase.task

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        if (task.title.isBlank()) {
            return Result.Error(Exception("Title cannot be empty"))

        }
        
        return taskRepository.updateTask(task)
    }
}