package com.shreyash.dotrack.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import com.shreyash.dotrack.MainActivity
import com.shreyash.dotrack.R
import com.shreyash.dotrack.TrackConstants

/**
 * Implementation of App Widget functionality.
 */
class TaskWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        // Refresh widget data when we receive a custom action
        if (intent.action == TrackConstants.ACTION_WIDGET_TASK_COMPLETE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                intent.component
            )
            
            // This forces the RemoteViewsService to reload
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Set up the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.task_widget)
            
            // Set title click to open the app
            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            
            val pendingOpenAppIntent = PendingIntent.getActivity(
                context, 0, openAppIntent, flags
            )
            
            views.setOnClickPendingIntent(R.id.widget_title, pendingOpenAppIntent)
            
            // Set up the intent for the ListView service
            val serviceIntent = Intent(context, TaskWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            
            // Set the RemoteAdapter for the ListView
            views.setRemoteAdapter(appWidgetId, R.id.widget_list_view, serviceIntent)
            views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view)
            
            // Set up PendingIntent template for task item clicks
            val clickPendingIntent = TaskWidgetClickHandler.getPendingSelfIntent(
                context,
                TrackConstants.ACTION_WIDGET_TASK_CLICK,
                appWidgetId
            )
            
            // Set up PendingIntent template for checkbox clicks
            val checkboxPendingIntent = TaskWidgetClickHandler.getPendingSelfIntent(
                context,
                TrackConstants.ACTION_WIDGET_TASK_COMPLETE,
                appWidgetId
            )
            
            // Set the PendingIntent templates for item clicks and checkbox clicks
            views.setPendingIntentTemplate(R.id.widget_list_view, clickPendingIntent)
            
            // Force data refresh when widget is updated
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view)
            
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}