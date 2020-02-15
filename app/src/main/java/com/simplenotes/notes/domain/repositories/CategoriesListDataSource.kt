package com.simplenotes.notes.domain.repositories

import com.simplenotes.notes.domain.models.Category

interface CategoriesListDataSource {

    /**
     * Retrieve the categories in domain model form
     */
    fun getCategories(): List<Category>

    /**
     * Refresh category data from the relevant source
     */
    fun refreshCategories()

    /**
     * Delete the specified category
     */
    fun deleteCategory(id: Int)
}