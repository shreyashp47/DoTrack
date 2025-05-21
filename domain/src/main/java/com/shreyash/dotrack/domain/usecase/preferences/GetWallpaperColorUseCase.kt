package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the wallpaper color
 */
class GetWallpaperColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Get the wallpaper color as a Flow
     * @return A Flow of hex color string (e.g., "#3A0CA3")
     */
    operator fun invoke(): Flow<String> {
        return userPreferencesRepository.getWallpaperColor()
    }
}