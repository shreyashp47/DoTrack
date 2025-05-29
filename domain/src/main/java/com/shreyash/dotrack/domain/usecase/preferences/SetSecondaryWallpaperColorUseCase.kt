package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Use case to set the wallpaper color
 */
class SetSecondaryWallpaperColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Set the wallpaper color
     * @param colorHex A hex color string (e.g., "#3A0CA3")
     */
    suspend operator fun invoke(colorHex: String): Result<Unit> {
        return userPreferencesRepository.setSecondaryWallpaperColor(colorHex)
    }
}