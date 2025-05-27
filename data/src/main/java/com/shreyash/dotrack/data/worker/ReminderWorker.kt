package com.shreyash.dotrack.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker class for handling task reminders
 * This worker is responsible for showing notifications when a task is due
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_TASK_ID = "taskId"
        const val KEY_TITLE = "title"
        private const val CHANNEL_ID = "reminder_channel"
        private const val CHANNEL_NAME = "Task Reminders"
        private const val CHANNEL_DESCRIPTION = "Notifications for task reminders"
        private const val TAG = "ReminderWorker"
    }

    override suspend fun doWork(): Result {
        try {
            val taskId = inputData.getString(KEY_TASK_ID) ?: return Result.failure()
            val title = inputData.getString(KEY_TITLE) ?: "Task Reminder"

            showNotification(taskId, title)
            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error showing notification", e)
            return Result.failure()
        }
    }

    private fun showNotification(taskId: String, title: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel (only needed on Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
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

        // Create a pending intent to open the app when notification is tapped
        val intent = applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the app's icon resource ID
        val iconResId = applicationContext.resources.getIdentifier(
            "ic_notification",
            "drawable",
            applicationContext.packageName
        )
        
        // Use a fallback icon if the app's icon is not found
        val icon = if (iconResId != 0) iconResId else android.R.drawable.ic_dialog_info

        // Build the notification
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle("Task Reminder")
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