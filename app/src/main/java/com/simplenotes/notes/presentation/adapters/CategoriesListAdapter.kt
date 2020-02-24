package com.simplenotes.notes.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.models.Category
import kotlinx.android.synthetic.main.item_category.view.*

const val VIEW_TYPE_DEFAULT = 0
const val VIEW_TYPE_SHOW_ALL = 1
const val VIEW_TYPE_NONE = 2
const val VIEW_TYPE_CREATE = 3

class CategoriesListAdapter(private val extraEntryAsShowAll: Boolean, private val clickListener: (Category, Boolean) -> Unit, private val longClickListener: (View, Category) -> Unit): RecyclerView.Adapter<CategoriesListAdapter.CategoryViewHolder>() {

    private var categories: MutableList<Category> = mutableListOf()

    /**
     * Inflate the view for items in the recycler
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutType = when (viewType) {
            VIEW_TYPE_NONE -> R.layout.item_category_none
            VIEW_TYPE_SHOW_ALL -> R.layout.item_category_show_all
            VIEW_TYPE_CREATE -> R.layout.item_category_create
            else -> R.layout.item_category
        }

        return CategoryViewHolder(layoutInflater.inflate(layoutType, parent, false))
    }

    /**
     * Identify the type of view to inflate
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && extraEntryAsShowAll) {
            VIEW_TYPE_SHOW_ALL
        } else {
            when (position) {
                0 -> VIEW_TYPE_NONE
                categories.size - 1 -> VIEW_TYPE_CREATE
                else -> VIEW_TYPE_DEFAULT
            }
        }
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
        fun bindItem(category: Category, clickListener: (Category, Boolean) -> Unit, longClickListener: (View, Category) -> Unit) {
            itemView.apply {

                textViewCategoryName.text = category.name

                if (category.id != 0) {
                    imageViewCategoryColour.setColorFilter(category.colour)
                    setOnLongClickListener { view ->
                        longClickListener(view, category)
                        return@setOnLongClickListener true      // Prevents other click handlers from being executed
                    }
                }

                setOnClickListener { clickListener(category, category == categories.last()) }
            }
        }
    }
}