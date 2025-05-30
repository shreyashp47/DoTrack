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
    fun getSecondaryWallpaperColor(): Flow<String>

    /**
     * Set the wallpaper color
     * @param colorHex A hex color string (e.g., "#3A0CA3")
     */
    suspend fun setWallpaperColor(colorHex: String): Result<Unit>
    suspend fun setSecondaryWallpaperColor(colorHex: String): Result<Unit>

    /**
     * Get the high priority task color as a Flow
     * Returns a hex color string (e.g., "#FFE7EA")
     */
    fun getHighPriorityColor(): Flow<String>
    
    /**
     * Set the high priority task color
     * @param colorHex A hex color string (e.g., "#FFE7EA")
     */
    suspend fun setHighPriorityColor(colorHex: String): Result<Unit>
    
    /**
     * Get the medium priority task color as a Flow
     * Returns a hex color string (e.g., "#FFF5D6")
     */
    fun getMediumPriorityColor(): Flow<String>
    
    /**
     * Set the medium priority task color
     * @param colorHex A hex color string (e.g., "#FFF5D6")
     */
    suspend fun setMediumPriorityColor(colorHex: String): Result<Unit>
    
    /**
     * Get the low priority task color as a Flow
     * Returns a hex color string (e.g., "#DFF5E0")
     */
    fun getLowPriorityColor(): Flow<String>
    
    /**
     * Set the low priority task color
     * @param colorHex A hex color string (e.g., "#DFF5E0")
     */
    suspend fun setLowPriorityColor(colorHex: String): Result<Unit>
}