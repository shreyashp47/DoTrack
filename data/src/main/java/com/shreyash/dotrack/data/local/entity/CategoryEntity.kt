package com.shreyash.dotrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreyash.dotrack.domain.model.Category
import java.util.UUID

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: Int
) {
    companion object {
        fun fromDomain(category: Category): CategoryEntity {
            return CategoryEntity(
                id = category.id,
                name = category.name,
                color = category.color
            )
        }
        
        fun createNew(
            name: String,
            color: Int
        ): CategoryEntity {
            return CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = name,
                color = color
            )
        }
    }
    
    fun toDomain(): Category {
        return Category(
            id = id,
            name = name,
            color = color
        )
    }
}