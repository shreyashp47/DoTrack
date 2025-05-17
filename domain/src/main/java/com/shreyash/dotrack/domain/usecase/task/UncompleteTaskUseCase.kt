package com.shreyash.dotrack.domain.usecase.task

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.TaskRepository
import javax.inject.Inject

class UncompleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return taskRepository.uncompleteTask(id)
    }
}