package com.simplenotes.notes.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.models.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesListAdapter(private val clickListener: (Category) -> Unit, private val longClickListener: (View, Category) -> Unit): RecyclerView.Adapter<CategoriesListAdapter.CategoryViewHolder>() {

    private var categories: MutableList<Category> = mutableListOf()

    /**
     * Inflate the view for items in the recycler
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CategoryViewHolder(layoutInflater.inflate(R.layout.item_category, parent, false))
    }

    /**
     * Returns the total number of items in the data set held by the adapter
     */
    override fun getItemCount(): Int {
        return categories.size
    }

    /**
     * Update UI with data values
     */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bindItem(category, clickListener, longClickListener)
    }

    /**
     * Update the attached categories with those provided (e.g. following a refresh)
     */
    fun setCategories(categoryList: List<Category>) {
        categories.clear()
        categories.addAll(categoryList)
        notifyDataSetChanged()
    }

    /**
     * ViewHolder subclass providing click binding functionality for the attached recycler view
     */
    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(category: Category, clickListener: (Category) -> Unit, longClickListener: (View, Category) -> Unit) {
            itemView.apply {

                textViewCategoryName.text = category.name

                // Hide the colour icon for an empty 'none' category
                if (category.id == 0) {
                    imageViewCategoryColour.visibility = View.INVISIBLE

                // Otherwise set the appropriate colour and add a long click listener which can be used to trigger edit options
                } else {
                    imageViewCategoryColour.visibility = View.VISIBLE
                    imageViewCategoryColour.setColorFilter(category.colour)

                    setOnLongClickListener { view ->
                        longClickListener(view, category)
                        return@setOnLongClickListener true      // Prevents other click handlers from being executed
                    }
                }

                setOnClickListener { clickListener(category) }
            }
        }
    }
}