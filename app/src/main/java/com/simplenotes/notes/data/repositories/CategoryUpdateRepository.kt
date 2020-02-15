package com.simplenotes.notes.data.repositories

import com.simplenotes.notes.data.local.*
import com.simplenotes.notes.domain.models.Category
import com.simplenotes.notes.domain.repositories.CategoryUpdateDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class CategoryUpdateRepository(private val context: DataSourceContext): CategoryUpdateDataSource {

    private val categoryDao = getCategoryDao()

    private fun getCategoryDao(): CategoryEntityDao {
        val db = NoteDatabase.getDatabase(context.application)
        return db?.categoryDao()!!
    }

    /**
     * Retrieve the appropriate category in domain model form
     */
    override fun getCategory(id: Int?): Category? = runBlocking(Dispatchers.IO) {
        return@runBlocking when (id) {
            null -> null
            else -> categoryDao.getCategoryFromId(id).asDomainModel()
        }
    }

    /**
     * Create/update a category with the required data
     */
    override fun updateCategory(id: Int?, name: String, colour: Int) {
        runBlocking(Dispatchers.IO) {
            when (id) {
                null -> categoryDao.insertCategory(CategoryEntity(null, name, colour))
                else -> categoryDao.updateCategory(CategoryEntity(id, name, colour))
            }
        }
    }
}