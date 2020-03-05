package com.simplenotes.notes.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.simplenotes.notes.domain.models.Note
import java.time.LocalDateTime

@Entity(foreignKeys = [ForeignKey(entity = CategoryEntity::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("categoryId"),
                        onDelete = ForeignKey.CASCADE)])

data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var title: String?,
    var content: String,
    var createdDateTime: LocalDateTime,
    @ColumnInfo(index = true)   // Additional index on the foreign key so this can be referenced as the left-most column for efficiency
    val categoryId: Int?
)

/**
 * Mapper to convert from database model to domain model
 */
fun NoteEntity.asDomainModel(relatedCategory: CategoryEntity?): Note {
    return Note(id = this.id!!,
        title = this.title,
        content = this.content,
        createdDateTime = this.createdDateTime,
        category = relatedCategory?.asDomainModel())
}

/**
 * Mapper to convert from database model to domain model
 */
fun List<NoteEntity>.asDomainModel(relatedCategories: List<CategoryEntity>): List<Note> {
    return map {
        Note(id = it.id!!,
            title = it.title,
            content = it.content,
            createdDateTime = it.createdDateTime,
            category = getCategoryForId(it.categoryId, relatedCategories)?.asDomainModel())
    }
}

/**
 * Find the related category for id specified on the note entity
 */
private fun getCategoryForId(id: Int?, relatedCategories: List<CategoryEntity>): CategoryEntity? {
    if (id == null) {
        return null
    }

    for (category in relatedCategories) {
        if (category.id == id) {
            return category
        }
    }
    return null
}