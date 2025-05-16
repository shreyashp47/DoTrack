package com.shreyash.dotrack.domain.usecase

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<Result<List<Task>>> {
        return taskRepository.getTasks()
    }
}