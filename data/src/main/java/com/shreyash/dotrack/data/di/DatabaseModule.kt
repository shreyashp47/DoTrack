package com.shreyash.dotrack.data.di

import android.content.Context
import androidx.room.Room
import com.shreyash.dotrack.data.local.TaskDatabase
import com.shreyash.dotrack.data.local.dao.CategoryDao
import com.shreyash.dotrack.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "dotrack_database"
        ).build()
    }
    
    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }
    
    @Provides
    fun provideCategoryDao(database: TaskDatabase): CategoryDao {
        return database.categoryDao()
    }
}