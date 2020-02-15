package com.simplenotes.notes.domain.repositories

import com.simplenotes.notes.domain.models.Note

interface NotesListDataSource {

    /**
     * Retrieve the appropriate notes in domain model form
     */
    fun getNotes(): List<Note>

    /**
     * Refresh notes data from the relevant source
     */
    fun refreshNotes()

    /**
     * Delete the specified note
     */
    fun deleteNote(id: Int)
}