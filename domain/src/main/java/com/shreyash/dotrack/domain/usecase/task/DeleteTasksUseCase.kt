package com.shreyash.dotrack.domain.usecase.task

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.TaskDeleteScope
import com.shreyash.dotrack.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(scope: TaskDeleteScope): Result<Unit> {
        return when (scope) {
            TaskDeleteScope.ALL -> taskRepository.deleteAllTasks()
            TaskDeleteScope.COMPLETED -> taskRepository.deleteCompletedTasks()
            TaskDeleteScope.INCOMPLETE -> taskRepository.deleteIncompleteTasks()
        }
    }
}
