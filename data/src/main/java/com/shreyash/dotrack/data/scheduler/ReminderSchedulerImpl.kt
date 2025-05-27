package com.shreyash.dotrack.data.scheduler

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.shreyash.dotrack.data.worker.ReminderWorker
import com.shreyash.dotrack.domain.ReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the ReminderScheduler interface using WorkManager
 * This class is responsible for scheduling and canceling reminders
 */
@Singleton
class ReminderSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : ReminderScheduler {

    override fun scheduleReminder(taskId: String, title: String, dueDate: LocalDateTime) {
        // Launch in a coroutine to avoid blocking the main thread
        CoroutineScope(ioDispatcher).launch {
            val triggerTime = dueDate.minusMinutes(30) // 30 minutes before due time
            val delay = Duration.between(LocalDateTime.now(), triggerTime).toMillis()
            if (delay > 0) {
                val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(workDataOf(
                        ReminderWorker.KEY_TASK_ID to taskId,
                        ReminderWorker.KEY_TITLE to title
                    ))
                    .addTag(taskId)
                    .build()

                WorkManager.getInstance(context).enqueue(workRequest)
            }
        }
    }

    override fun cancelReminder(taskId: String) {
        // Launch in a coroutine to avoid blocking the main thread
        CoroutineScope(ioDispatcher).launch {
            WorkManager.getInstance(context).cancelAllWorkByTag(taskId)
        }
    }
}