package com.shreyash.dotrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shreyash.dotrack.data.local.dao.TaskDao
import com.shreyash.dotrack.data.local.entity.TaskEntity
import com.shreyash.dotrack.data.util.DateTimeConverters

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}