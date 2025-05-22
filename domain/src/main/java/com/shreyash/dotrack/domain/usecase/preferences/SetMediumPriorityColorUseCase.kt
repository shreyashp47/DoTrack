package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Use case to set the medium priority task color
 */
class SetMediumPriorityColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(colorHex: String): Result<Unit> {
        return userPreferencesRepository.setMediumPriorityColor(colorHex)
    }
}