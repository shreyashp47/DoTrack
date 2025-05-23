package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Use case to set the low priority task color
 */
class SetLowPriorityColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(colorHex: String): Result<Unit> {
        return userPreferencesRepository.setLowPriorityColor(colorHex)
    }
}