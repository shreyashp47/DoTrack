package com.shreyash.dotrack.data.local.converter

import androidx.room.TypeConverter
import com.shreyash.dotrack.domain.model.Priority

class PriorityConverters {
    @TypeConverter
    fun fromPriority(priority: Priority): Int {
        return priority.value
    }

    @TypeConverter
    fun toPriority(value: Int): Priority {
        return Priority.values().first { it.value == value }
    }
}

