package com.shreyash.dotrack.domain.repository

import com.shreyash.dotrack.core.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user preferences
 */
interface UserPreferencesRepository {
    /**
     * Get the auto wallpaper setting as a Flow
     */
    fun getAutoWallpaperEnabled(): Flow<Boolean>

    /**
     * Set the auto wallpaper setting
     */
    suspend fun setAutoWallpaperEnabled(enabled: Boolean): Result<Unit>

    /**
     * Get the wallpaper color as a Flow
     * Returns a hex color string (e.g., "#3A0CA3")
     */
    fun getWallpaperColor(): Flow<String>

    /**
     * Set the wallpaper color
     * @param colorHex A hex color string (e.g., "#3A0CA3")
     */
    suspend fun setWallpaperColor(colorHex: String): Result<Unit>
}