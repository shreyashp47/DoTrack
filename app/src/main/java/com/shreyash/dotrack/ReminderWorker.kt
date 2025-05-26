package com.shreyash.dotrack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

// app/worker/ReminderWorker.kt
class ReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        try {
            val taskId = inputData.getString("taskId") ?: return Result.failure()
            val title = inputData.getString("title") ?: "Task Reminder"

            showNotification(taskId, title)
            return Result.success()
        } catch (e: Exception) {
            // Log the error
            Log.e("ReminderWorker", "Error showing notification", e)
            return Result.failure()
        }
    }

    private fun showNotification(taskId: String, title: String) {
        val channelId = "reminder_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel (only needed on Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, 
                "Task Reminders", 
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for task reminders"
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

        // Build the notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
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
