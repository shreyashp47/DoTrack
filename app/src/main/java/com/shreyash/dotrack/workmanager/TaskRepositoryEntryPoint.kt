package com.shreyash.dotrack.workmanager

import com.shreyash.dotrack.domain.repository.TaskRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TaskRepositoryEntryPoint {
    fun taskRepository(): TaskRepository
}
