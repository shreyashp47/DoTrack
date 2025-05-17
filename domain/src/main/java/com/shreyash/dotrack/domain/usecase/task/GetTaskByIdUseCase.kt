package com.shreyash.dotrack.domain.usecase.task

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(id: String): Flow<Result<Task>> {
        return taskRepository.getTaskById(id)
    }
}