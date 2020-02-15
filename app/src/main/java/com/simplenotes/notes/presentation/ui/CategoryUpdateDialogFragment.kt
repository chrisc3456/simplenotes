package com.simplenotes.notes.presentation.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.models.Category
import com.simplenotes.notes.presentation.factories.CategoryUpdateViewModelFactory
import com.simplenotes.notes.presentation.viewmodels.CategoryUpdateViewModel
import com.simplenotes.notes.presentation.viewmodels.SharedCategoryChangedViewModel

class CategoryUpdateDialogFragment: DialogFragment() {

    private var categoryId: Int? = null
    private lateinit var updateViewModel: CategoryUpdateViewModel
    private val categoriesChangedViewModel: SharedCategoryChangedViewModel by navGraphViewModels(R.id.nav_main)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        categoryId = CategoryUpdateDialogFragmentArgs.fromBundle(requireArguments()).categoryId?.toInt()

        val inflater = requireActivity().layoutInflater
        val builder = AlertDialog.Builder(requireContext())
            .setView(inflater.inflate(R.layout.fragment_category_update_dialog, null))
            .setTitle(getTitle())
            .setNegativeButton(resources.getString(R.string.dialog_button_cancel), null)
            .setPositiveButton(resources.getString(R.string.dialog_button_save)) { _, _ -> saveNote() }

        return builder.create()
    }

    /**
     * When overriding onCreateDialog, onCreateView must still return a non-null view in order for onViewCreated to be called
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category_update_dialog, container)
    }

    /**
     * Setting up the view model here rather than onCreateDialog as observers require access to the lifecycle owner's view which is null until after onCreateView
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        addObservers()
    }

    /**
     * Provide a title for the dialog based on whether an existing category id has been provided
     */
    private fun getTitle(): String =
        when (categoryId) {
            null -> resources.getString(R.string.dialog_title_category_new)
            else -> resources.getString(R.string.dialog_title_category_update)
        }

    /**
     * Create the view models
     */
    private fun setupViewModel() {
        updateViewModel = ViewModelProvider(this, CategoryUpdateViewModelFactory(requireActivity().application, categoryId)).get(CategoryUpdateViewModel::class.java)
    }

    /**
     * Set up observers on the view model live data outputs
     */
    private fun addObservers() {
        updateViewModel.category.observe(viewLifecycleOwner, Observer { result -> onCategoryRefreshed(result) })
    }

    /**
     * Update the display once the relevant category has been obtained via the view model
     */
    private fun onCategoryRefreshed(category: Category) {
        requireDialog().findViewById<EditText>(R.id.editTextCategoryName).setText(category.name)
        selectChipWithColour(category.colour)
    }

    /**
     * Save the entered details
     */
    private fun saveNote() {
        val specifiedName = requireDialog().findViewById<EditText>(R.id.editTextCategoryName).text.toString()
        updateViewModel.saveCategory(specifiedName, getSelectedColour())
        categoriesChangedViewModel.categoryUpdated()
    }

    /**
     * Get the colour of the selected chip
     */
    private fun getSelectedColour(): Int {
        val chipGroup = requireDialog().findViewById<ChipGroup>(R.id.chipGroupCategoryColour)
        val selectedChip = requireDialog().findViewById<Chip>(chipGroup.checkedChipId)
        return selectedChip.chipBackgroundColor?.defaultColor!!
    }

    /**
     * Find and select the chip with the supplied colour value
     */
    private fun selectChipWithColour(colour: Int) {
        val chipGroup = requireDialog().findViewById<ChipGroup>(R.id.chipGroupCategoryColour)
        for (i in 0..chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.chipBackgroundColor?.defaultColor == colour) {
                chipGroup.check(chip.id)
                break
            }
        }
    }
}