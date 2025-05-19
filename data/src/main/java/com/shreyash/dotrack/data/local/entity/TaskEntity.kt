package com.shreyash.dotrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val dueDate: LocalDateTime,
    val priority: Priority,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(task: Task): TaskEntity {
            return TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                isCompleted = task.isCompleted,
                dueDate = task.dueDate,
                priority = task.priority,
                createdAt = task.createdAt,
                updatedAt = task.updatedAt
            )
        }
        
        fun createNew(
            title: String,
            description: String,
            dueDate: LocalDateTime,
            priority: Priority
        ): TaskEntity {
            val now = LocalDateTime.now()
            return TaskEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                isCompleted = false,
                dueDate = dueDate,
                priority = priority,
                createdAt = now,
                updatedAt = now
            )
        }
    }
    
    fun toDomain(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            isCompleted = isCompleted,
            dueDate = dueDate,
            priority = priority,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}