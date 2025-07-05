package com.shreyash.dotrack.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.shreyash.dotrack.R
import com.shreyash.dotrack.TrackConstants
import com.shreyash.dotrack.data.local.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                
                if (taskId != null) {
                    // Update task in database
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val database = TaskDatabase.getInstance(context.applicationContext)
                            val taskDao = database.taskDao()
                            
                            if (isCompleted) {
                                taskDao.completeTask(taskId)
                            } else {
                                taskDao.uncompleteTask(taskId)
                            }
                            
                            // Show feedback on main thread
                            CoroutineScope(Dispatchers.Main).launch {
                                val action = if (isCompleted) "completed" else "uncompleted"
                                Toast.makeText(context, "Task $action", Toast.LENGTH_SHORT).show()
                            }
                            
                            // Force widget to update on main thread
                            CoroutineScope(Dispatchers.Main).launch {
                                updateWidget(context)
                            }
                        } catch (e: Exception) {
                            Log.e("TaskWidgetReceiver", "Error updating task completion", e)
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(context, "Error updating task", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
    
    private fun updateWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, TaskWidgetProvider::class.java)
        )
        
        // Notify that the data has changed
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view)
        
        // Trigger widget update
        val updateIntent = Intent(context, TaskWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }
        context.sendBroadcast(updateIntent)
    }
}