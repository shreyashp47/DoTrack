package com.shreyash.dotrack.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.shreyash.dotrack.core.ui.theme.DEFAULT_BOTTOM_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_HIGH_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_LOW_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_MEDIUM_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_TOP_COLOR
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// data/repository/UserPreferencesRepositoryImpl.kt
@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val AUTO_WALLPAPER_ENABLED = booleanPreferencesKey("auto_wallpaper_enabled")
        val WALLPAPER_COLOR = stringPreferencesKey("wallpaper_color")
        val SECONDARY_WALLPAPER_COLOR = stringPreferencesKey("secondary_wallpaper_color")
        val HIGH_PRIORITY_COLOR = stringPreferencesKey("high_priority_color")
        val MEDIUM_PRIORITY_COLOR = stringPreferencesKey("medium_priority_color")
        val LOW_PRIORITY_COLOR = stringPreferencesKey("low_priority_color")
    }

    private val TAG = "UserPreferencesRepositoryImpl"

    override fun getAutoWallpaperEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.AUTO_WALLPAPER_ENABLED] ?: false
            }
    }

    override suspend fun setAutoWallpaperEnabled(enabled: Boolean): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.AUTO_WALLPAPER_ENABLED] = enabled
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getWallpaperColor(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.WALLPAPER_COLOR] ?: DEFAULT_TOP_COLOR
            }
    }

    override fun getSecondaryWallpaperColor(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.SECONDARY_WALLPAPER_COLOR] ?: DEFAULT_BOTTOM_COLOR
            }
    }

    override suspend fun setWallpaperColor(colorHex: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.WALLPAPER_COLOR] = colorHex
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun setSecondaryWallpaperColor(colorHex: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.SECONDARY_WALLPAPER_COLOR] = colorHex
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getHighPriorityColor(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.HIGH_PRIORITY_COLOR] ?: DEFAULT_HIGH_PRIORITY_COLOR
            }
    }

    override suspend fun setHighPriorityColor(colorHex: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.HIGH_PRIORITY_COLOR] = colorHex
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getMediumPriorityColor(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.MEDIUM_PRIORITY_COLOR] ?: DEFAULT_MEDIUM_PRIORITY_COLOR
            }
    }

    override suspend fun setMediumPriorityColor(colorHex: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.MEDIUM_PRIORITY_COLOR] = colorHex
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getLowPriorityColor(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.LOW_PRIORITY_COLOR] ?: DEFAULT_LOW_PRIORITY_COLOR
            }
    }

    override suspend fun setLowPriorityColor(colorHex: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.LOW_PRIORITY_COLOR] = colorHex
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
