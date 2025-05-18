package com.shreyash.dotrack.domain.usecase.category

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return categoryRepository.deleteCategory(id)
    }
}