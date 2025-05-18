package com.shreyash.dotrack.domain.repository

import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<Result<List<Category>>>
    fun getCategoryById(id: String): Flow<Result<Category>>
    suspend fun addCategory(name: String, color: Int): Result<Unit>
    suspend fun updateCategory(category: Category): Result<Unit>
    suspend fun deleteCategory(id: String): Result<Unit>
}