package com.simplenotes.notes.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.simplenotes.notes.domain.factories.CategoriesListDataSourceFactory
import com.simplenotes.notes.domain.models.Category

class CategoriesListViewModel(application: Application): AndroidViewModel(application) {

    val categories = MutableLiveData<MutableList<Category>>().apply { value = mutableListOf() }
    private val dataSource = CategoriesListDataSourceFactory.createDataSource(application)

    init {
        refreshCategories()
    }

    /**
     * Refresh the categories from the data source, including an extra 'dummy' entry for cases where no category is required
     */
    fun refreshCategories() {
        dataSource.refreshCategories()
        categories.value!!.add(Category(0, "None", 0))
        categories.value!!.addAll(dataSource.getCategories())
    }

    /**
     * Perform a delete of the provided category
     */
    fun deleteCategory(categoryId: Int) {
        dataSource.deleteCategory(categoryId)
    }
}