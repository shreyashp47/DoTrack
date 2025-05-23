package com.shreyash.dotrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shreyash.dotrack.data.local.converter.DateTimeConverters
import com.shreyash.dotrack.data.local.converter.PriorityConverters
import com.shreyash.dotrack.data.local.dao.CategoryDao
import com.shreyash.dotrack.data.local.dao.TaskDao
import com.shreyash.dotrack.data.local.entity.CategoryEntity
import com.shreyash.dotrack.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, CategoryEntity::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class, PriorityConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
}