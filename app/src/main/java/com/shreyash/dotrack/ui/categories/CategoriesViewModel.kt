package com.shreyash.dotrack.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Category
import com.shreyash.dotrack.domain.usecase.category.AddCategoryUseCase
import com.shreyash.dotrack.domain.usecase.category.GetCategoriesUseCase
import com.shreyash.dotrack.domain.usecase.category.GetCategoryByIdUseCase
import com.shreyash.dotrack.domain.usecase.category.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    val categories: StateFlow<Result<List<Category>>> = getCategoriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    fun loadCategory(id: String, onLoaded: (Category) -> Unit) {
        viewModelScope.launch {
            val result = getCategoryByIdUseCase(id).first()
            if (result.isSuccess()) {
                result.getOrNull()?.let(onLoaded)
            }
        }
    }

    suspend fun addCategory(name: String): Result<Unit> {
        return addCategoryUseCase(name, color = 0xFF64B5F6.toInt())
    }

    suspend fun updateCategory(id: String, name: String): Result<Unit> {
        return updateCategoryUseCase(Category(id = id, name = name, color = 0xFF64B5F6.toInt()))
    }
}