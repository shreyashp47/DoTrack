package com.shreyash.dotrack.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.shreyash.dotrack.MainActivity
import com.shreyash.dotrack.TrackConstants

object TaskWidgetClickHandler {
    
    /**
     * Creates a PendingIntent to handle widget item clicks
     */
    fun getPendingSelfIntent(context: Context, action: String, appWidgetId: Int): PendingIntent {
        val intent = Intent(context, TaskWidgetClickReceiver::class.java).apply {
            this.action = action
            putExtra(TrackConstants.EXTRA_WIDGET_ID, appWidgetId)
        }
        
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        
        return PendingIntent.getBroadcast(context, 0, intent, flags)
    }
    
    /**
     * Creates a PendingIntent to open the app's main activity
     */
    fun getOpenAppIntent(context: Context, taskId: String?): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            
            if (taskId != null) {
                action = TrackConstants.ACTION_VIEW_TASK
                putExtra(TrackConstants.EXTRA_TASK_ID, taskId)
            }
        }
        
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        
        return PendingIntent.getActivity(context, 0, intent, flags)
    }
}