package com.simplenotes.notes.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.simplenotes.notes.domain.factories.CategoryUpdateDataSourceFactory
import com.simplenotes.notes.domain.models.Category

class CategoryUpdateViewModel(application: Application, categoryId: Int?): AndroidViewModel(application) {

    private var id: Int? = categoryId
    val category = MutableLiveData<Category>()
    private val dataSource = CategoryUpdateDataSourceFactory.createDataSource(application)

    init {
        getNote()
    }

    /**
     * Retrieve the note from the data source
     */
    private fun getNote() {
        if (id != null) {
            category.value = dataSource.getCategory(id!!)
        }
    }

    /**
     * Ask the data source to create/update the required category details
     */
    fun saveCategory(name: String, colour: Int) {
        dataSource.updateCategory(id, name, colour)
    }
}