package com.shreyash.dotrack.workmanager

import android.content.Context
import com.shreyash.dotrack.domain.ReminderScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

// data/di/ReminderModule.kt
@Module
@InstallIn(SingletonComponent::class)
object ReminderModule {

    @Provides
    fun provideReminderScheduler(
        @ApplicationContext context: Context
    ): ReminderScheduler = ReminderSchedulerImpl(context)
}