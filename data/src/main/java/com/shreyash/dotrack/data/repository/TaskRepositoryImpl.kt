package com.shreyash.dotrack.data.repository

import com.shreyash.dotrack.core.di.IoDispatcher
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.data.local.dao.TaskDao
import com.shreyash.dotrack.data.local.entity.TaskEntity
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {

    override fun getTasks(): Flow<Result<List<Task>>> {
        return taskDao.getTasks()
            .map { entities -> Result.success(entities.map { it.toDomain() }) }
            .catch { e -> emit(Result.error(e)) }
            .flowOn(ioDispatcher)
    }

    override fun getTaskById(id: String): Flow<Result<Task>> {
        return taskDao.getTaskById(id)
            .map { entity ->
                entity?.let {
                    Result.success(it.toDomain())
                } ?: Result.error(NoSuchElementException("Task not found"))
            }
            .catch { e -> emit(Result.error(e)) }
            .flowOn(ioDispatcher)
    }

    override suspend fun addTask(
        title: String,
        description: String,
        dueDate: LocalDateTime?,
        priority: Priority,
        reminderEnabled: Boolean
    ): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            val task = TaskEntity.createNew(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority,
                reminderEnabled = reminderEnabled
            )
            taskDao.insertTask(task)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            taskDao.updateTask(TaskEntity.fromDomain(task))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun deleteTask(id: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            taskDao.deleteTask(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun deleteAllTask(): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            taskDao.deleteAllTask()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun completeTask(id: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            taskDao.completeTask(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun uncompleteTask(id: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            taskDao.uncompleteTask(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun disableReminder(id: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            taskDao.disableReminder(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
}