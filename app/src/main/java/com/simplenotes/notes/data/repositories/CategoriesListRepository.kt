package com.simplenotes.notes.data.repositories

import com.simplenotes.notes.data.local.CategoryEntity
import com.simplenotes.notes.data.local.CategoryEntityDao
import com.simplenotes.notes.data.local.NoteDatabase
import com.simplenotes.notes.data.local.asDomainModel
import com.simplenotes.notes.domain.models.Category
import com.simplenotes.notes.domain.repositories.CategoriesListDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class CategoriesListRepository(private val context: DataSourceContext): CategoriesListDataSource {

    private var categories: List<CategoryEntity> = emptyList()
    private val categoryDao = getCategoryDao()

    private fun getCategoryDao(): CategoryEntityDao {
        val db = NoteDatabase.getDatabase(context.application)
        return db?.categoryDao()!!
    }

    /**
     * Retrieve the categories in domain model form
     */
    override fun getCategories(): List<Category> {
        return categories.asDomainModel()
    }

    /**
     * Refresh category data from the relevant source
     */
    override fun refreshCategories() {
        refreshCategoriesFromLocalDb()
    }

    /**
     * Refresh category data from the local db. Note - this runs on the IO thread
     */
    private fun refreshCategoriesFromLocalDb() {
        runBlocking(Dispatchers.IO) {
            categories = categoryDao.getAllCategories()
        }
    }

    /**
     * Delete the specified category. Note - this runs on the IO thread
     */
    override fun deleteCategory(id: Int) {
        runBlocking(Dispatchers.IO) {
            val existingCategory = categoryDao.getCategoryFromId(id)
            categoryDao.deleteCategory(existingCategory)
        }
    }
}