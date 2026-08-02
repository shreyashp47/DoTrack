package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<String> {
        return userPreferencesRepository.getLanguage()
    }
}
