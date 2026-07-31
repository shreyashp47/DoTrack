package com.shreyash.dotrack.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.shreyash.dotrack.R

object TaskWidgetUpdater {

    fun updateTaskWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, TaskWidgetProvider::class.java)
        )

        if (appWidgetIds.isNotEmpty()) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view)
        }
    }
}
