package com.simplenotes.notes.presentation.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simplenotes.notes.presentation.viewmodels.NoteUpdateViewModel

class NoteUpdateViewModelFactory(private val application: Application, private val noteId: Int): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteUpdateViewModel(application, noteId) as T
    }
}