package com.shreyash.dotrack.widget.util

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.shreyash.dotrack.R
import com.shreyash.dotrack.widget.TaskWidgetProvider

class WidgetUtils {
    fun isWidgetActive(context: Context): Boolean {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetComponent = ComponentName(context, TaskWidgetProvider::class.java)
        val widgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)

        return widgetIds.isNotEmpty()
    }

    fun notifyTaskWidgetUpdate(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)

        val componentName = ComponentName(context, TaskWidgetProvider::class.java)
        val widgetIds = appWidgetManager.getAppWidgetIds(componentName)

        // Refresh widget list view data
        //appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.task_list_view)

        // Optionally trigger widget update if needed
        val updateIntent = Intent(context, TaskWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
        }
        context.sendBroadcast(updateIntent)
    }

}