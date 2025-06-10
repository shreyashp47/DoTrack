package com.shreyash.dotrack.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.shreyash.dotrack.TrackConstants

/**
 * Broadcast receiver to handle widget item clicks
 */
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
                
                // In a real implementation, you would use your repository to update the task
                // For now, we'll just show a toast
                var action = if (isCompleted) "completed" else "uncompleted"
                Toast.makeText(context, "Task $action", Toast.LENGTH_SHORT).show()
                
                // Force widget to update
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, TaskWidgetProvider::class.java)
                )
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, android.R.id.list)
                
                // Update the widget
                val updateIntent = Intent(context, TaskWidgetProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                }
                context.sendBroadcast(updateIntent)
            }
        }
    }
}