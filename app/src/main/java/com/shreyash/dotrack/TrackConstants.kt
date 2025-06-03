package com.shreyash.dotrack

import android.util.Log

object TrackConstants {

    const val DEBUG_REMINDER_TIME = 1L
    const val REMINDER_TIME = 30L

    fun getReminderTime(): Long {
        return  REMINDER_TIME
    }


}