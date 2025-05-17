package com.shreyash.dotrack.data.di

import com.shreyash.dotrack.data.repository.CategoryRepositoryImpl
import com.shreyash.dotrack.data.repository.TaskRepositoryImpl
import com.shreyash.dotrack.domain.repository.CategoryRepository
import com.shreyash.dotrack.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository
    
    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}