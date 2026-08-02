package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.domain.model.SortOption
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the task list sort option
 */
class GetSortOptionUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Get the task list sort option as a Flow
     */
    operator fun invoke(): Flow<SortOption> {
        return userPreferencesRepository.getSortOption()
    }
}
