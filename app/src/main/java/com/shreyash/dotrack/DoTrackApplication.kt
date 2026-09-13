package com.shreyash.dotrack

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.shreyash.dotrack.domain.model.AppLanguage
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val SYNC_PREFS_NAME = "dotrack_sync_prefs"
        private const val KEY_LANGUAGE = "language_sync"
    }

    /**
     * Synchronous read of the persisted app language, used by MainActivity's
     * attachBaseContext to wrap the configuration before the first frame.
     * Uses SharedPreferences for fast sync read (no DataStore blocking).
     * DataStore remains source of truth; this cache is kept in sync via
     * UserPreferencesRepositoryImpl and onCreate migration.
     */
    fun getLanguageCodeSync(): String {
        return getSharedPreferences(SYNC_PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, AppLanguage.SYSTEM.value) ?: AppLanguage.SYSTEM.value
    }

    override fun onCreate() {
        super.onCreate()
        // One-time migration / keep SharedPreferences cache in sync with DataStore
        applicationScope.launch {
            try {
                val language = userPreferencesRepository.getLanguage().first()
                val prefs = getSharedPreferences(SYNC_PREFS_NAME, Context.MODE_PRIVATE)
                if (prefs.getString(KEY_LANGUAGE, null) != language) {
                    prefs.edit().putString(KEY_LANGUAGE, language).apply()
                }
            } catch (_: Exception) {
                // ignore - next launch will retry
            }
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}

