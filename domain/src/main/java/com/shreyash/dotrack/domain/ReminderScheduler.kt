package com.shreyash.dotrack.domain

import java.time.LocalDateTime

// domain/ReminderScheduler.kt
interface ReminderScheduler {
    fun scheduleReminder(taskId: String, title: String, dueDate: LocalDateTime)
    fun cancelReminder(taskId: String)
}
