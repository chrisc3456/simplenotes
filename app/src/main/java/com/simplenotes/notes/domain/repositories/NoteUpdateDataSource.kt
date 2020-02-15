package com.simplenotes.notes.domain.repositories

import com.simplenotes.notes.domain.models.Note

interface NoteUpdateDataSource {

    /**
     * Retrieve the appropriate note in domain model form
     */
    fun getNote(id: Int): Note?

    /**
     * Create/update a note with the required data
     */
    fun updateNote(id: Int?, title: String, content: String, categoryId: Int?)
}