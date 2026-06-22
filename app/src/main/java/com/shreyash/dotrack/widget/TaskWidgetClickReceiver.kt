package com.shreyash.dotrack.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.shreyash.dotrack.R
import com.shreyash.dotrack.TrackConstants
import java.util.concurrent.TimeUnit

class TaskWidgetClickReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            TrackConstants.ACTION_WIDGET_TASK_CLICK -> {
                val taskId = intent.getStringExtra(TrackConstants.EXTRA_TASK_ID)
                val openAppIntent = TaskWidgetClickHandler.getOpenAppIntent(context, taskId)

                try {
                    openAppIntent.send()
                } catch (e: Exception) {
                    Log.e("TaskWidgetReceiver", "Error opening task details", e)
                }
            }

            TrackConstants.ACTION_WIDGET_TASK_COMPLETE -> {
                val taskId = intent.getStringExtra(TrackConstants.EXTRA_TASK_ID)
                val isCompleted = intent.getBooleanExtra(TrackConstants.EXTRA_TASK_COMPLETED, false)

                if (taskId != null) {
                    val workRequest = OneTimeWorkRequestBuilder<TaskWidgetWorker>()
                        .setInputData(workDataOf(
                            "taskId" to taskId,
                            "isCompleted" to isCompleted
                        ))
                        .build()
                    WorkManager.getInstance(context).enqueue(workRequest)
                }
            }
        }
    }
}
