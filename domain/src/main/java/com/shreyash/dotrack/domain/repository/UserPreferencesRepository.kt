package com.shreyash.dotrack.domain.repository

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption
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

    /**
     * Get the dark mode preference as a Flow
     * Returns "system", "light", or "dark"
     */
    fun getDarkMode(): Flow<String>

    /**
     * Set the dark mode preference
     * @param mode "system", "light", or "dark"
     */
    suspend fun setDarkMode(mode: String): Result<Unit>

    /**
     * Get the task list sort option as a Flow
     */
    fun getSortOption(): Flow<SortOption>

    /**
     * Set the task list sort option
     */
    suspend fun setSortOption(option: SortOption): Result<Unit>

    /**
     * Get the task list sort direction as a Flow
     */
    fun getSortDirection(): Flow<SortDirection>

    /**
     * Set the task list sort direction
     */
    suspend fun setSortDirection(direction: SortDirection): Result<Unit>
}