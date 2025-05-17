package com.shreyash.dotrack.domain.usecase.category

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(name: String, color: Int): Result<Unit> {
        if (name.isBlank()) {
            val exception: Exception = Exception("Category name cannot be empty")
            return Result.Error(exception)
        }

        return categoryRepository.addCategory(name, color)
    }
}