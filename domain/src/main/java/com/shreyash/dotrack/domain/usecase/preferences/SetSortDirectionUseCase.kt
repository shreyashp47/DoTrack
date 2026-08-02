package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Use case to set the task list sort direction
 */
class SetSortDirectionUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Set the task list sort direction
     */
    suspend operator fun invoke(direction: SortDirection): Result<Unit> {
        return userPreferencesRepository.setSortDirection(direction)
    }
}
