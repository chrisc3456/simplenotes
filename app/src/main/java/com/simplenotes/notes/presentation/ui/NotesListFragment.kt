package com.simplenotes.notes.presentation.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.*
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.models.Category
import com.simplenotes.notes.presentation.adapters.NotesListAdapter
import com.simplenotes.notes.presentation.callbacks.NoteSwipeToDeleteBasicCallback
import com.simplenotes.notes.presentation.factories.NotesListViewModelFactory
import com.simplenotes.notes.presentation.utils.NotesListViewStyle
import com.simplenotes.notes.presentation.utils.NotesListViewStyleHandler
import com.simplenotes.notes.presentation.viewmodels.NotesListViewModel
import com.simplenotes.notes.presentation.viewmodels.SharedCategorySelectedViewModel
import com.simplenotes.notes.presentation.viewmodels.SharedNotesChangedViewModel
import kotlinx.android.synthetic.main.fragment_notes_list.*

class NotesListFragment : Fragment() {

    private val changeNotifyViewModel: SharedNotesChangedViewModel by navGraphViewModels(R.id.nav_main)
    private val selectCategoryViewModel: SharedCategorySelectedViewModel by navGraphViewModels(R.id.nav_main)
    private lateinit var listViewModel: NotesListViewModel
    private val notesAdapter = NotesListAdapter { noteId: Int -> onClickedNote(noteId) }
    private var isFilteringCategory = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notes_list, container, false)
        setupViewModels()
        addObservers()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setupRecycler()
        fabNew.setOnClickListener { onClickedNote(0) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notes_list, menu)

        // Set up search function
        val searchItem = menu.findItem(R.id.menuNotesSearch)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                notesAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNotesSettings -> openSettings()
            R.id.menuNotesCategory -> openCategorySelect()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Set up the toolbar and associate with the activity support action bar, including navigation controller support
     */
    private fun setupActionBar() {
        setHasOptionsMenu(true)

        val hostActivity = requireActivity() as AppCompatActivity
        hostActivity.setSupportActionBar(toolbarNotesList)
        hostActivity.setupActionBarWithNavController(findNavController())
        hostActivity.supportActionBar?.title = resources.getString(R.string.title_all_notes)
        hostActivity.supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    /**
     * Create the view models
     */
    private fun setupViewModels() {
        listViewModel = ViewModelProvider(this, NotesListViewModelFactory(requireActivity().application)).get(NotesListViewModel::class.java)
    }

    /**
     * Set up observers on the view model live data outputs
     */
    private fun addObservers() {
        listViewModel.notes.observe(viewLifecycleOwner, Observer { results -> notesAdapter.setNotes(results) })
        changeNotifyViewModel.noteChanged.observe(viewLifecycleOwner, Observer { changed -> onNotesChanged(changed) })
        selectCategoryViewModel.categorySelected.observe(viewLifecycleOwner, Observer { selectedCategory -> onSelectedCategory(selectedCategory) })
    }

    /**
     * Set up the recycler view with appropriate adapter, layout manager and swipe actions
     */
    private fun setupRecycler() {
        recyclerNotes.apply {
            if (NotesListViewStyleHandler.getNotesListViewStyle(context) == NotesListViewStyle.VIEW_STYLE_LIST) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(createDividerDecorator(DividerItemDecoration.VERTICAL, R.drawable.divider_horizontal_trans))
            } else {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                addItemDecoration(createDividerDecorator(DividerItemDecoration.HORIZONTAL, R.drawable.divider_vertical_trans))
            }
        }

        recyclerNotes.adapter = notesAdapter
        setupSwipeActionDelete()
    }

    /**
     * Set up the callback for handling delete swipe action, along with defining call to perform the delete
     */
    private fun setupSwipeActionDelete() {
        val swipeDeleteHandler = object : NoteSwipeToDeleteBasicCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                listViewModel.deleteNote(notesAdapter.getSelectedNoteId(viewHolder.adapterPosition))
            }
        }

        val touchHelperDelete = ItemTouchHelper(swipeDeleteHandler)
        touchHelperDelete.attachToRecyclerView(recyclerNotes)
    }

    /**
     * Create a divider decorator for the recycler view to separate items
     */
    private fun createDividerDecorator(orientation: Int, drawableId: Int): DividerItemDecoration {
        val decorator = DividerItemDecoration(context, orientation)
        decorator.setDrawable(ContextCompat.getDrawable(requireContext(), drawableId)!!)
        return decorator
    }

    /**
     * Navigate to the fragment for editing the selected note, passing the id, or for creating a new one
     */
    private fun onClickedNote(id: Int) {
        val action = NotesListFragmentDirections.actionNotesListFragmentToNoteUpdateFragment(id)
        findNavController().navigate(action)
    }

    /**
     * Refresh the notes display if notified that one has changed
     */
    private fun onNotesChanged(changed: Boolean) {
        if (changed) {
            listViewModel.refreshNotes()
        }
    }

    /**
     * Update the toolbar title and filter the search results when a category is selected
     */
    private fun onSelectedCategory(selectedCategory: Category) {
        if (isFilteringCategory) {
            isFilteringCategory = false
            val hostActivity = requireActivity() as AppCompatActivity
            if (selectedCategory.id == 0) {
                hostActivity.supportActionBar?.title = resources.getString(R.string.title_all_notes)
                listViewModel.refreshNotesForCategory(null)
            } else {
                hostActivity.supportActionBar?.title = selectedCategory.name
                listViewModel.refreshNotesForCategory(selectedCategory.id)
            }
        }
    }

    /**
     * Navigate to the settings fragment
     */
    private fun openSettings() {
        findNavController().navigate(NotesListFragmentDirections.actionNotesListFragmentToSettingsFragment())
    }

    /**
     * Navigate to the category select fragment
     */
    private fun openCategorySelect() {
        isFilteringCategory = true
        val action = NotesListFragmentDirections.actionNotesListFragmentToCategoriesListFragment()
        action.extraEntryAsShowAll = true
        findNavController().navigate(action)
    }
}
