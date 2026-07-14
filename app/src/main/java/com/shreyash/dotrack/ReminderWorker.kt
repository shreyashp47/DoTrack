package com.shreyash.dotrack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shreyash.dotrack.R
import com.shreyash.dotrack.domain.repository.TaskRepository
import com.shreyash.dotrack.widget.TaskWidgetUpdater
import com.shreyash.dotrack.workmanager.TaskRepositoryEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// app/worker/ReminderWorker.kt
class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val taskRepository: TaskRepository by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            TaskRepositoryEntryPoint::class.java
        ).taskRepository()
    }

    private val CHANNEL_ID = applicationContext.getString(R.string.reminder_channel_id)
    private val CHANNEL_NAME = applicationContext.getString(R.string.reminder_channel_name)
    private val CHANNEL_DESCRIPTION = applicationContext.getString(R.string.reminder_channel_desc)

    companion object {
        const val KEY_TASK_ID = "taskId"
        const val KEY_TITLE = "title"
        private const val TAG = "ReminderWorker"
    }

    override suspend fun doWork(): Result {
        try {
            val taskId = inputData.getString(KEY_TASK_ID) ?: return Result.failure()
            val title = inputData.getString(KEY_TITLE) ?: applicationContext.getString(R.string.task_reminder)

            withContext(Dispatchers.IO) {
                try {
                    taskRepository.disableReminder(taskId)
                    Log.d(TAG, "Reminder disabled for task: $taskId")
                    
                    // Update widgets after disabling reminder
                    withContext(Dispatchers.Main) {
                        TaskWidgetUpdater.updateTaskWidgets(applicationContext)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error disabling reminder for task: $taskId", e)
                }
            }



            showNotification(taskId, title)
            return Result.success()
        } catch (e: Exception) {
            Log.e("ReminderWorker", "Error showing notification", e)
            return Result.failure()
        }
    }


    private fun showNotification(taskId: String, title: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel (only needed on Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create a deep link intent to open the task detail screen
        val intent =
            applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName)
                ?.apply {
                    // Add action and task ID for deep linking
                    action = "com.shreyash.dotrack.OPEN_TASK"
                    putExtra("task_id", taskId)
                    // Add flags to handle the case when the app is already running
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            taskId.hashCode(), // Use taskId hash as request code to ensure uniqueness
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val icon = R.drawable.ic_launcher_foreground

        // Build the notification
        val notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        )
            .setSmallIcon(icon)
            .setContentTitle(applicationContext.getString(R.string.task_reminder))
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        // Show the notification
        notificationManager.notify(taskId.hashCode(), notification)
    }
}
