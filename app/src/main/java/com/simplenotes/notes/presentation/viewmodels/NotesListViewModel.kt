package com.simplenotes.notes.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.simplenotes.notes.domain.factories.NotesListDataSourceFactory
import com.simplenotes.notes.domain.models.Note

class NotesListViewModel(application: Application): AndroidViewModel(application) {

    val notes = MutableLiveData<List<Note>>()
    private val dataSource = NotesListDataSourceFactory.createDataSource(application)
    private var lastCategoryId: Int? = null

    init {
        refreshNotes()
    }

    /**
     * Refresh the notes from the data source
     */
    fun refreshNotes() {
        dataSource.refreshNotes(lastCategoryId)
        notes.value = dataSource.getNotes()
    }

    /**
     * Refresh the notes from the data source for a particular category
     */
    fun refreshNotesForCategory(categoryId: Int?) {
        lastCategoryId = categoryId
        refreshNotes()
    }

    /**
     * Ask the data source to delete the specified note
     */
    fun deleteNote(id: Int) {
        dataSource.deleteNote(id)
        refreshNotes()
    }
}