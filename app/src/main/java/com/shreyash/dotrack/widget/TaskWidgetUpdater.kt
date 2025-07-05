package com.shreyash.dotrack.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.shreyash.dotrack.R

/**
 * Utility class to update task widgets when data changes
 */
object TaskWidgetUpdater {
    
    /**
     * Updates all task widgets when task data changes
     */
    fun updateTaskWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, TaskWidgetProvider::class.java)
        )
        
        if (appWidgetIds.isNotEmpty()) {
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
    
    /**
     * Forces a complete refresh of all task widgets
     */
    fun forceRefreshTaskWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, TaskWidgetProvider::class.java)
        )
        
        // Update each widget individually
        for (appWidgetId in appWidgetIds) {
            TaskWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}