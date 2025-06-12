package com.shreyash.dotrack

import android.util.Log

object TrackConstants {

    const val DEBUG_REMINDER_TIME = 1L
    const val REMINDER_TIME = 30L

    // Widget related constants
    const val ACTION_WIDGET_TASK_CLICK = "com.shreyash.dotrack.ACTION_WIDGET_TASK_CLICK"
    const val ACTION_WIDGET_TASK_COMPLETE = "com.shreyash.dotrack.ACTION_WIDGET_TASK_COMPLETE"
    const val ACTION_VIEW_TASK = "com.shreyash.dotrack.ACTION_VIEW_TASK"
    const val EXTRA_TASK_ID = "com.shreyash.dotrack.EXTRA_TASK_ID"
    const val EXTRA_TASK_COMPLETED = "com.shreyash.dotrack.EXTRA_TASK_COMPLETED"
    const val EXTRA_WIDGET_ID = "com.shreyash.dotrack.EXTRA_WIDGET_ID"

    fun getReminderTime(): Long {
        return REMINDER_TIME
    }
}