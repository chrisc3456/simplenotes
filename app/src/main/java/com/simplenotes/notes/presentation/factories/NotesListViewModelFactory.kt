package com.simplenotes.notes.presentation.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simplenotes.notes.presentation.viewmodels.NotesListViewModel

class NotesListViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotesListViewModel(application) as T
    }
}