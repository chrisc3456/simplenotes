package com.simplenotes.notes.data.local

import androidx.room.*

@Dao
interface CategoryEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryEntity: CategoryEntity)

    @Update
    fun updateCategory(categoryEntity: CategoryEntity)

    @Delete
    fun deleteCategory(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM CategoryEntity")
    fun getAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity WHERE id = :categoryId")
    fun getCategoryFromId(categoryId: Int): CategoryEntity
}