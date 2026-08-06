package com.shreyash.dotrack.domain.usecase.preferences

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(language: String): Result<Unit> {
        return userPreferencesRepository.setLanguage(language)
    }
}
