package com.shreyash.dotrack.domain.usecase.category

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Category
import com.shreyash.dotrack.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Unit> {
        if (category.name.isBlank()) {
            val exception: Exception = Exception("Category name cannot be empty")
            return Result.Error(exception)
        }

        return categoryRepository.updateCategory(category)
    }
}