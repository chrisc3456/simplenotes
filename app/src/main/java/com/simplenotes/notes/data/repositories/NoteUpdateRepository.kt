package com.simplenotes.notes.data.repositories

import com.simplenotes.notes.data.local.*
import com.simplenotes.notes.domain.models.Note
import com.simplenotes.notes.domain.repositories.NoteUpdateDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class NoteUpdateRepository(private val context: DataSourceContext): NoteUpdateDataSource {

    private val noteDao = getNoteDao()
    private val categoryDao = getCategoryDao()

    private fun getNoteDao(): NoteEntityDao {
        val db = NoteDatabase.getDatabase(context.application)
        return db?.noteDao()!!
    }

    private fun getCategoryDao(): CategoryEntityDao {
        val db = NoteDatabase.getDatabase(context.application)
        return db?.categoryDao()!!
    }

    /**
     * Retrieve the appropriate note in domain model form
     */
    override fun getNote(id: Int): Note? = runBlocking(Dispatchers.IO) {
        val note = noteDao.getNoteFromId(id)
        var category: CategoryEntity? = null
        if (note?.categoryId != null) {
            category = getRelatedCategory(note.categoryId)
        }

        return@runBlocking when (note) {
            null -> null
            else -> note.asDomainModel(category)
        }
    }

    /**
     * Look up the category associated with the required note
     */
    private fun getRelatedCategory(categoryId: Int): CategoryEntity? = runBlocking(Dispatchers.IO) {
        categoryDao.getCategoryFromId(categoryId)
    }

    /**
     * Create/update a note with the required data
     */
    override fun updateNote(id: Int?, title: String, content: String, categoryId: Int?) {
        runBlocking(Dispatchers.IO) {
            when (id) {
                null -> noteDao.insertNote(NoteEntity(null, title, content, LocalDateTime.now(), categoryId))
                else -> noteDao.updateNote(NoteEntity(id, title, content, LocalDateTime.now(), categoryId))
            }
        }
    }
}