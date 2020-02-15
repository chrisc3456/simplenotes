package com.simplenotes.notes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simplenotes.notes.domain.models.Category

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val colour: Int
)

/**
 * Mapper to convert from database model to domain model
 */
fun CategoryEntity.asDomainModel(): Category {
    return Category(id = this.id!!,
        name = this.name,
        colour = this.colour)
}

/**
 * Mapper to convert from database model to domain model
 */
fun List<CategoryEntity>.asDomainModel(): List<Category> {
    return map {
        Category(id = it.id!!,
            name = it.name,
            colour = it.colour)
    }
}