package com.shreyash.dotrack

import android.util.Log

object TrackConstants {

    const val DEBUG_REMINDER_TIME = 1L
    const val REMINDER_TIME = 30L

    fun getReminderTime(): Long {
        return REMINDER_TIME // Use production time for release builds
    }

    fun TrackLogs(message: String){
        // Only log in debug builds - ProGuard will remove this in release
        Log.d("TRACK", message)
    }
}