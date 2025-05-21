package com.shreyash.dotrack.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
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
    }
    
    // Default wallpaper color
    private val DEFAULT_WALLPAPER_COLOR = "#3A0CA3"

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
                preferences[PreferencesKeys.WALLPAPER_COLOR] ?: DEFAULT_WALLPAPER_COLOR
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
}
