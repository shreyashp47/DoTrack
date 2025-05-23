package com.shreyash.dotrack.domain.repository

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TaskRepository {
    fun getTasks(): Flow<Result<List<Task>>>
    fun getTaskById(id: String): Flow<Result<Task>>
    suspend fun addTask(
        title: String,
        description: String,
        dueDate: LocalDateTime? = null,
        priority: Priority
    ): Result<Unit>

    suspend fun updateTask(task: Task): Result<Unit>
    suspend fun deleteTask(id: String): Result<Unit>
    suspend fun deleteAllTask(): Result<Unit>
    suspend fun completeTask(id: String): Result<Unit>
    suspend fun uncompleteTask(id: String): Result<Unit>
}