package com.simplenotes.notes.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.factories.CategoriesListDataSourceFactory
import com.simplenotes.notes.domain.models.Category

class CategoriesListViewModel(val app: Application, private val extraEntryAsShowAll: Boolean): AndroidViewModel(app) {

    val categories = MutableLiveData<List<Category>>()
    private val dataSource = CategoriesListDataSourceFactory.createDataSource(app)

    init {
        refreshCategories()
    }

    /**
     * Refresh the categories from the data source, including an extra 'dummy' entry for cases where no category is required
     */
    fun refreshCategories() {
        dataSource.refreshCategories()
        val results: MutableList<Category> = mutableListOf()

        addExtraCategoryPre(results)
        results.addAll(dataSource.getCategories())
        addExtraCategoryPost(results)

        categories.value = results
    }

    /**
     * Perform a delete of the provided category
     */
    fun deleteCategory(categoryId: Int) {
        dataSource.deleteCategory(categoryId)
    }

    /**
     * Create a dummy category for null selection if required
     */
    private fun addExtraCategoryPre(results: MutableList<Category>) {
        if (extraEntryAsShowAll) {
            results.add(Category(0, app.resources.getString(R.string.item_category_show_all), 0))
        } else {
            results.add(Category(0, app.resources.getString(R.string.item_category_none), 0))
        }
    }

    /**
     * Create a dummy category to represent creating a new category
     */
    private fun addExtraCategoryPost(results: MutableList<Category>) {
        results.add(Category(0, app.resources.getString(R.string.item_category_create), 0))
    }
}