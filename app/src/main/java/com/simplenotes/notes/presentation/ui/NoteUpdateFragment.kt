package com.simplenotes.notes.presentation.ui

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.setupActionBarWithNavController
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.models.Category
import com.simplenotes.notes.domain.models.Note
import com.simplenotes.notes.presentation.factories.NoteUpdateViewModelFactory
import com.simplenotes.notes.presentation.utils.KeyboardManager
import com.simplenotes.notes.presentation.viewmodels.NoteUpdateViewModel
import com.simplenotes.notes.presentation.viewmodels.SharedCategorySelectedViewModel
import com.simplenotes.notes.presentation.viewmodels.SharedNotesChangedViewModel
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.fragment_note_update.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NoteUpdateFragment : Fragment() {

    private var noteId: Int = 0
    private var categoryId: Int? = null
    private val categorySelectedViewModel: SharedCategorySelectedViewModel by navGraphViewModels(R.id.nav_main)
    private val changeNotifyViewModel: SharedNotesChangedViewModel by navGraphViewModels(R.id.nav_main)
    private lateinit var updateViewModel: NoteUpdateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_note_update, container, false)
        noteId = NoteUpdateFragmentArgs.fromBundle(requireArguments()).noteId

        setupViewModel()
        addObservers()
        setupBackNavigationCallback()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
    }

    /**
     * Inflate the options menu for the fragment
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_note_update, menu)
    }

    /**
     * Support pressing of the back button using navigation including any custom callbacks (see setupBackNavigationCallback)
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.menuCategorySelect -> getCategory()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Set up the toolbar and associate with the activity support action bar, including navigation controller support
     */
    private fun setupActionBar() {
        setHasOptionsMenu(true)

        val hostActivity = requireActivity() as AppCompatActivity
        hostActivity.setSupportActionBar(toolbarNoteUpdate)
        hostActivity.setupActionBarWithNavController(findNavController())

        val actionBar = hostActivity.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    /**
     * Create the view models
     */
    private fun setupViewModel() {
        updateViewModel = ViewModelProvider(this, NoteUpdateViewModelFactory(requireActivity().application, noteId)).get(
            NoteUpdateViewModel::class.java)
    }

    /**
     * Set up observers on the view model live data outputs
     */
    private fun addObservers() {
        updateViewModel.note.observe(viewLifecycleOwner, Observer { result ->
            onNoteRefreshed(result)
        })

        categorySelectedViewModel.categorySelected.observe(viewLifecycleOwner, Observer { category ->
            onCategorySelected(category)
        })
    }

    /**
     * Set up a call back to save when the back navigation is triggered
     */
    private fun setupBackNavigationCallback() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (editTextContent.text.isNotBlank()) {
                saveNote()
            }
            notifyNoteChanged()
            KeyboardManager.hideKeyboard(requireActivity(), requireView())
            findNavController().getViewModelStoreOwner(R.id.nav_main).viewModelStore.clear()    // Prevents shared view models being retained with existing values
            findNavController().popBackStack()
        }
        callback.isEnabled = true
    }

    /**
     * Update the display once the relevant note has been obtained via the view model
     */
    private fun onNoteRefreshed(note: Note) {
        val detailFragment = childFragmentManager.findFragmentById(R.id.fragmentNoteDetail) as NoteDetailFragment
        detailFragment.displayNote(note)
        categoryId = note.category?.id
    }

    /**
     * Save the details entered to the database
     */
    private fun saveNote() = runBlocking(Dispatchers.IO) {
        updateViewModel.saveNote(
            editTextTitle.text.toString(),
            editTextContent.text.toString(),
            categoryId
        )
    }

    /**
     * Notify shared view model of a change to a note
     */
    private fun notifyNoteChanged() {
        changeNotifyViewModel.noteUpdated()
    }

    /**
     * Navigate to another fragment to allow selection of the required category
     */
    private fun getCategory() {
        // Using string for the argument as navigation components doesn't support nullable integers
        val action = NoteUpdateFragmentDirections.actionNoteUpdateFragmentToCategoriesListFragment()
        action.categoryId = categoryId.toString()
        findNavController().navigate(action)
    }

    /**
     * Update the local category id with that which has been selected for linking the note
     */
    private fun onCategorySelected(category: Category) {
        categoryId = if (category.id == 0) {
            null
        } else {
            category.id
        }
        val detailFragment = childFragmentManager.findFragmentById(R.id.fragmentNoteDetail) as NoteDetailFragment
        detailFragment.displayCategory(category)
    }
}
