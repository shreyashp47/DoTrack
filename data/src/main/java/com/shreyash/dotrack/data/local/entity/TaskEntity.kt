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
    val dueDate: LocalDateTime? = null,
    val priority: Priority,
    val reminderEnabled: Boolean = false,
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
                reminderEnabled = task.reminderEnabled,
                createdAt = task.createdAt,
                updatedAt = task.updatedAt
            )
        }
        
        fun createNew(
            title: String,
            description: String,
            dueDate: LocalDateTime? = null,
            priority: Priority,
            reminderEnabled: Boolean = false
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
                updatedAt = now,
                reminderEnabled = reminderEnabled
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
            reminderEnabled = reminderEnabled,
            updatedAt = updatedAt
        )
    }
}