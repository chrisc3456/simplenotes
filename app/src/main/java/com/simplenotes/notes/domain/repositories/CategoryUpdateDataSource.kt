package com.simplenotes.notes.domain.repositories

import com.simplenotes.notes.domain.models.Category

interface CategoryUpdateDataSource {

    /**
     * Retrieve the appropriate category in domain model form
     */
    fun getCategory(id: Int?): Category?

    /**
     * Create/update a category with the required data
     */
    fun updateCategory(id: Int?, name: String, colour: Int)
}