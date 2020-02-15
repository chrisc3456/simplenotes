package com.simplenotes.notes.data.repositories

import com.simplenotes.notes.data.local.*
import com.simplenotes.notes.domain.models.Note
import com.simplenotes.notes.domain.repositories.NotesListDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NotesListRepository(private val context: DataSourceContext): NotesListDataSource {

    private var notes: List<NoteEntity> = emptyList()
    private val noteDao = getNoteDao()

    private var categories: List<CategoryEntity> = emptyList()
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
     * Retrieve the appropriate notes in domain model form
     */
    override fun getNotes(): List<Note> {
        return notes.asDomainModel(categories)
    }

    /**
     * Refresh notes data from the relevant source
     */
    override fun refreshNotes() {
        refreshNotesFromLocalDb()
        refreshCategoryEntitiesFromLocalDb()
    }

    /**
     * Refresh notes data from the local db. Note - this runs on the IO thread
     */
    private fun refreshNotesFromLocalDb() {
        runBlocking(Dispatchers.IO) {
            notes = noteDao.getAllNotes()
        }
    }

    /**
     * Refresh category data from the local db. Note - this runs on the IO thread
     */
    private fun refreshCategoryEntitiesFromLocalDb() {
        runBlocking(Dispatchers.IO) {
            categories = categoryDao.getAllCategories()
        }
    }

    /**
     * Delete the specified note. Note - this runs on the IO thread
     */
    override fun deleteNote(id: Int) {
        runBlocking(Dispatchers.IO) {
            val existingNote = noteDao.getNoteFromId(id)
            if (existingNote != null) {
                noteDao.deleteNote(existingNote)
            }
        }
    }
}