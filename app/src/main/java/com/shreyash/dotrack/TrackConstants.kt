package com.shreyash.dotrack

import android.util.Log
import com.squareup.leakcanary.core.BuildConfig

object TrackConstants {

    const val DEBUG_REMINDER_TIME = 1L
    const val REMINDER_TIME = 30L

    fun getReminderTime(): Long {
        return if (BuildConfig.DEBUG) DEBUG_REMINDER_TIME else REMINDER_TIME
    }

    fun TrackLogs(message: String){
        if(BuildConfig.DEBUG) Log.d("TRACK", message)
    }
}