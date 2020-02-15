package com.simplenotes.notes.presentation.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simplenotes.notes.presentation.viewmodels.CategoriesListViewModel

class CategoriesListViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoriesListViewModel(application) as T
    }
}