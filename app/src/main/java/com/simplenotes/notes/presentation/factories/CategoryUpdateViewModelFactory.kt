package com.simplenotes.notes.presentation.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simplenotes.notes.presentation.viewmodels.CategoryUpdateViewModel

class CategoryUpdateViewModelFactory(private val application: Application, private val categoryId: Int?): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryUpdateViewModel(application, categoryId) as T
    }
}