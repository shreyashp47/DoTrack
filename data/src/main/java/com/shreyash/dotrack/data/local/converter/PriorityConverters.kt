package com.shreyash.dotrack.data.local.converter

import androidx.room.TypeConverter
import com.shreyash.dotrack.domain.model.Priority

class PriorityConverters {
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }
    
    @TypeConverter
    fun toPriority(value: String): Priority {
        return Priority.valueOf(value)
    }
}