package com.simplenotes.notes.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.models.Category
import com.simplenotes.notes.domain.models.Note
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.fragment_note_update.*

class NoteDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    /**
     * Display the specified note details in the appropriate fields
     */
    fun displayNote(note: Note) {
        editTextTitle.setText(note.title)
        editTextContent.setText(note.content)
        displayCategory(note.category)
    }

    /**
     * Display the specified category
     */
    fun displayCategory(category: Category?) {
        val parentFrag = parentFragment as NoteUpdateFragment
        if (category == null || category.id == 0) {
            parentFrag.apply {
                dividerNoteColour.visibility = View.GONE
                textViewSelectedCategory.visibility = View.GONE
            }
        } else {
            parentFrag.apply {
                dividerNoteColour.visibility = View.VISIBLE
                dividerNoteColour.setBackgroundColor(category.colour)
                textViewSelectedCategory.visibility = View.VISIBLE
                textViewSelectedCategory.text = category.name
            }
        }
    }
}
