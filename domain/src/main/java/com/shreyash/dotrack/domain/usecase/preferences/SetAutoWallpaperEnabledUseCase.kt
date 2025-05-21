package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Use case to set the auto wallpaper enabled setting
 */
class SetAutoWallpaperEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Set the auto wallpaper enabled setting
     */
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        return userPreferencesRepository.setAutoWallpaperEnabled(enabled)
    }
}