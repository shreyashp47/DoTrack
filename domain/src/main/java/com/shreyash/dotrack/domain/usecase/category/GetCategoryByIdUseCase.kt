package com.shreyash.dotrack.domain.usecase.category

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Category
import com.shreyash.dotrack.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(id: String): Flow<Result<Category>> {
        return categoryRepository.getCategoryById(id)
    }
}