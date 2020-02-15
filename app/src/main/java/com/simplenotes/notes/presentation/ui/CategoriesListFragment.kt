package com.simplenotes.notes.presentation.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.models.Category
import com.simplenotes.notes.presentation.adapters.CategoriesListAdapter
import com.simplenotes.notes.presentation.callbacks.DefaultActionModeCallback
import com.simplenotes.notes.presentation.callbacks.OnActionItemClickListener
import com.simplenotes.notes.presentation.factories.CategoriesListViewModelFactory
import com.simplenotes.notes.presentation.viewmodels.CategoriesListViewModel
import com.simplenotes.notes.presentation.viewmodels.SharedCategoryChangedViewModel
import com.simplenotes.notes.presentation.viewmodels.SharedCategorySelectedViewModel
import kotlinx.android.synthetic.main.fragment_categories_list.*
import kotlinx.android.synthetic.main.item_category_create.*

class CategoriesListFragment : Fragment(), OnActionItemClickListener {

    private var selectedCategoryId: Int? = null
    private val actionModeCallback = DefaultActionModeCallback(this)
    private val selectedNotifyViewModel: SharedCategorySelectedViewModel by navGraphViewModels(R.id.nav_main)
    private val categoriesChangedViewModel: SharedCategoryChangedViewModel by navGraphViewModels(R.id.nav_main)
    private lateinit var listViewModel: CategoriesListViewModel
    private val categoriesAdapter = CategoriesListAdapter( { category: Category -> onClickedCategory(category) },
        { view: View, category: Category -> onLongClickedCategory(view, category) })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories_list, container, false)
        setupViewModels()
        addObservers()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupActionBar()
        constraintCreateCategory.setOnClickListener { newCategory() }
    }

    /**
     * Create the view models
     */
    private fun setupViewModels() {
        listViewModel = ViewModelProvider(this, CategoriesListViewModelFactory(requireActivity().application)).get(CategoriesListViewModel::class.java)
    }

    /**
     * Set up observers on the view model live data outputs
     */
    private fun addObservers() {
        listViewModel.categories.observe(viewLifecycleOwner, Observer { results -> categoriesAdapter.setCategories(results) })
        categoriesChangedViewModel.categoryChanged.observe(viewLifecycleOwner, Observer { changed -> onCategoriesChanged(changed) })
    }

    /**
     * Set up the recycler view with appropriate adapter, layout manager and swipe actions
     */
    private fun setupRecycler() {
        recyclerCategories.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply { setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_horizontal_dark)!!) })
        }
    }

    /**
     * Set up the toolbar and associate with the activity support action bar, including navigation controller support
     */
    private fun setupActionBar() {
        setHasOptionsMenu(true)

        val hostActivity = requireActivity() as AppCompatActivity
        hostActivity.setSupportActionBar(toolbarCategoriesList)
        hostActivity.setupActionBarWithNavController(findNavController())

        val actionBar = hostActivity.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = resources.getString(R.string.title_category_select)
    }

    /**
     * Triggered on click of an item in the options menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onActionItemClick(item)
        return super.onOptionsItemSelected(item)
    }

    /**
     * Perform functions against action items triggered from context menu
     */
    override fun onActionItemClick(item: MenuItem) {
        when (item.itemId) {
            android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.menuCategoryEdit -> editCategory()
            R.id.menuCategoryDelete -> deleteCategory()
        }
    }

    /**
     * Show custom dialog for creating a category
     */
    private fun newCategory() {
        findNavController().navigate(CategoriesListFragmentDirections.actionCategoriesListFragmentToCategoryUpdateDialogFragment())
    }

    /**
     * Show custom dialog for updating a category
     */
    private fun editCategory() {
        val action = CategoriesListFragmentDirections.actionCategoriesListFragmentToCategoryUpdateDialogFragment()
        action.categoryId = when (val value = selectedCategoryId) {
            null -> null
            else -> value.toString()
        }
        findNavController().navigate(action)
    }

    /**
     * Delete the selected category
     */
    private fun deleteCategory() {
        listViewModel.deleteCategory(selectedCategoryId!!)
        categoriesChangedViewModel.categoryUpdated()

        val toast = Toast.makeText(context, "Category deleted", Toast.LENGTH_SHORT)
        toast.show()
        //TODO: Need to update linked notes when deleting the category
    }

    /**
     * Refresh the categories display if notified that one has changed
     */
    private fun onCategoriesChanged(changed: Boolean) {
        if (changed) {
            listViewModel.refreshCategories()
        }
    }

    /**
     * When a category is clicked, close any open action mode, update the shared view model and go back to previous navigation destination
     */
    private fun onClickedCategory(category: Category) {
        selectedCategoryId = category.id
        actionModeCallback.finishActionMode()
        selectedNotifyViewModel.categorySelected(category)
        findNavController().popBackStack()
    }

    /**
     * When a category is long clicked, start a new action mode to display a context menu
     */
    private fun onLongClickedCategory(view: View, category: Category) {
        selectedCategoryId = category.id
        actionModeCallback.finishActionMode()
        actionModeCallback.startActionMode(view, R.menu.menu_categories_list_context, category.name)
    }
}
