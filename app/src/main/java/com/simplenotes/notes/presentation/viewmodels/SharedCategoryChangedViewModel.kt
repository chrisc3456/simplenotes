package com.simplenotes.notes.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedCategoryChangedViewModel: ViewModel() {

    val categoryChanged = MutableLiveData<Boolean>()

    fun categoryUpdated() {
        categoryChanged.value = true
    }
}