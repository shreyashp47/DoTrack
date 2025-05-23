package com.shreyash.dotrack.workmanager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.shreyash.dotrack.ReminderWorker
import com.shreyash.dotrack.domain.ReminderScheduler
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// data/reminder/ReminderSchedulerImpl.kt
public class ReminderSchedulerImpl @Inject constructor(
    private val context: Context
) : ReminderScheduler {

    override fun scheduleReminder(taskId: String, title: String, dueDate: LocalDateTime) {
        val triggerTime = dueDate.minusMinutes(30) // 🔔 30 minutes before
        val delay = Duration.between(LocalDateTime.now(), triggerTime).toMillis()
        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf(
                    "taskId" to taskId,
                    "title" to title
                ))
                .addTag(taskId)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    override fun cancelReminder(taskId: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(taskId)
    }
}