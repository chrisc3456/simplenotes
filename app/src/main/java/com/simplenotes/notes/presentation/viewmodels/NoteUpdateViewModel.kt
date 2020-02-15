package com.simplenotes.notes.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.simplenotes.notes.domain.factories.NoteUpdateDataSourceFactory
import com.simplenotes.notes.domain.models.Note

class NoteUpdateViewModel(application: Application, noteId: Int): AndroidViewModel(application) {

    private var id: Int? = null     // Navigation doesn't support nullable integer args so convert here
    val note = MutableLiveData<Note>()
    private val dataSource = NoteUpdateDataSourceFactory.createDataSource(application)

    init {
        if (noteId != 0) {
            id = noteId
        }
        getNote()
    }

    /**
     * Retrieve the note from the data source
     */
    private fun getNote() {
        if (id != null) {
            note.value = dataSource.getNote(id!!)
        }
    }

    /**
     * Ask the data source to create/update the required note details
     */
    fun saveNote(title: String, content: String, categoryId: Int?) {
        dataSource.updateNote(id, title, content, categoryId)
    }
}