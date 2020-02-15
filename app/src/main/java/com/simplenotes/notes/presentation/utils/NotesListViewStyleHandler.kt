package com.simplenotes.notes.presentation.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.simplenotes.notes.R

object NotesListViewStyleHandler {

    /**
     * Provide an enum representation of the view style preference
     */
    fun getNotesListViewStyle(context: Context): NotesListViewStyle =
        when (getNotesViewStylePreference(context)) {
            "Grid" -> NotesListViewStyle.VIEW_STYLE_GRID
            "List" -> NotesListViewStyle.VIEW_STYLE_LIST
            else -> NotesListViewStyle.VIEW_STYLE_LIST
        }

    /**
     * Retrieve the list/grid view style preference for the notes list from shared preferences
     */
    private fun getNotesViewStylePreference(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val viewStylePreference = sharedPreferences.getString(context.resources.getString(R.string.settings_key_notes_display), null)
        return viewStylePreference.toString()
    }
}