package com.shreyash.dotrack.data.repository

import com.shreyash.dotrack.core.di.IoDispatcher
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.data.local.dao.CategoryDao
import com.shreyash.dotrack.data.local.entity.CategoryEntity
import com.shreyash.dotrack.domain.model.Category
import com.shreyash.dotrack.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CategoryRepository {
    
    override fun getCategories(): Flow<Result<List<Category>>> {
        return categoryDao.getCategories()
            .map { entities -> Result.success(entities.map { it.toDomain() }) }
            .catch { e -> emit(Result.error(e)) }
            .flowOn(ioDispatcher)
    }
    
    override fun getCategoryById(id: String): Flow<Result<Category>> {
        return categoryDao.getCategoryById(id)
            .map { entity -> 
                entity?.let { 
                    Result.success(it.toDomain()) 
                } ?: Result.error(NoSuchElementException("Category not found"))
            }
            .catch { e -> emit(Result.error(e)) }
            .flowOn(ioDispatcher)
    }
    
    override suspend fun addCategory(name: String, color: Int): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            val category = CategoryEntity.createNew(
                name = name,
                color = color
            )
            categoryDao.insertCategory(category)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun updateCategory(category: Category): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            categoryDao.updateCategory(CategoryEntity.fromDomain(category))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun deleteCategory(id: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            categoryDao.deleteCategory(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
}