package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.SortOption
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Use case to set the task list sort option
 */
class SetSortOptionUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Set the task list sort option
     */
    suspend operator fun invoke(option: SortOption): Result<Unit> {
        return userPreferencesRepository.setSortOption(option)
    }
}
