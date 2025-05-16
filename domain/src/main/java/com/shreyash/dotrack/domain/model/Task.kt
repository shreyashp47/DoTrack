package com.shreyash.dotrack.domain.model

import java.time.LocalDateTime

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val dueDate: LocalDateTime?,
    val priority: Priority,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

enum class Priority {
    LOW, MEDIUM, HIGH
}