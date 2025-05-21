package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the auto wallpaper enabled setting
 */
class GetAutoWallpaperEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Get the auto wallpaper enabled setting as a Flow
     */
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesRepository.getAutoWallpaperEnabled()
    }
}