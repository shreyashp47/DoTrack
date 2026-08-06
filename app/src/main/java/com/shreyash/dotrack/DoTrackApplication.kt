package com.shreyash.dotrack

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Application class for the DoTrack app
 * This class initializes Hilt and WorkManager
 */
@HiltAndroidApp
class DoTrackApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    /**
     * Synchronous read of the persisted app language, used by MainActivity's
     * attachBaseContext to wrap the configuration before the first frame.
     */
    fun getLanguageCodeSync(): String = runBlocking {
        userPreferencesRepository.getLanguage().first()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}

