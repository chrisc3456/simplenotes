package com.simplenotes.notes.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simplenotes.notes.domain.models.Category

class SharedCategorySelectedViewModel: ViewModel() {

    val categorySelected = MutableLiveData<Category>()

    fun categorySelected(category: Category) {
        categorySelected.value = category
    }
}