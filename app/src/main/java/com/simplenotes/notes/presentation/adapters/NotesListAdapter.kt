package com.simplenotes.notes.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.simplenotes.notes.R
import com.simplenotes.notes.domain.models.Note
import com.simplenotes.notes.presentation.utils.NotesListViewStyle
import com.simplenotes.notes.presentation.utils.NotesListViewStyleHandler
import kotlinx.android.synthetic.main.item_note.view.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class NotesListAdapter(private val clickListener: (Int) -> Unit): RecyclerView.Adapter<NotesListAdapter.NoteViewHolder>(), Filterable {

    private var notesResults: MutableList<Note> = mutableListOf()
    private var notesUnfiltered: MutableList<Note> = mutableListOf()

    /**
     * Inflate the view for items in the recycler
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(layoutInflater.inflate(R.layout.item_note, parent, false))
    }

    /**
     * Returns the total number of items in the data set held by the adapter
     */
    override fun getItemCount(): Int {
        return notesResults.size
    }

    /**
     * Update UI with data values
     */
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesResults[position]
        holder.bindItem(note, clickListener)
    }

    /**
     * Format date and time for display
     */
    private fun formatDateTime(dateTime: LocalDateTime): String {
        val today = LocalDateTime.now()
        return when (val timeBetween = ChronoUnit.DAYS.between(dateTime, today).toInt()) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> "$timeBetween days ago"
        }
    }

    /**
     * Update the attached notes with those provided (e.g. following a refresh)
     */
    fun setNotes(noteList: List<Note>) {
        notesResults.clear()
        notesResults.addAll(noteList)
        notesUnfiltered.clear()
        notesUnfiltered.addAll(noteList)
        notifyDataSetChanged()
    }

    /**
     * Provides the unique id of the note at the specified position
     */
    fun getSelectedNoteId(position: Int): Int {
        return notesResults[position].id
    }

    /**
     * Returns a filter that can be used to constrain data with a filtering pattern
     */
    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Note>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(notesUnfiltered)
                }
                else {
                    val defaultLocale = Locale.getDefault()
                    val filterPattern = constraint.toString().toLowerCase(defaultLocale).trim()
                    for (note in notesResults) {
                        if (note.title?.toLowerCase(defaultLocale)!!.contains(filterPattern) || note.content.toLowerCase(defaultLocale).contains(filterPattern)) {
                            filteredList.add(note)
                        }
                    }
                }

                return FilterResults().apply { values = filteredList }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notesResults.clear()
                notesResults.addAll(results?.values as MutableList<Note>)
                notifyDataSetChanged()
            }
        }
    }

    /**
     * ViewHolder subclass providing click binding functionality for the attached recycler view
     */
    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(note: Note, clickListener: (Int) -> Unit) {

            itemView.apply {
                setOnClickListener { clickListener(note.id) }

                textViewTitle.text = getDisplayTitle(note.title)
                textViewContent.text = getDisplayContent(note.content)

                // Hide the category label if not set on the note
                if (note.category == null) {
                    textViewCategory.visibility = View.INVISIBLE
                    constraintEndBorder.setBackgroundColor(resources.getColor(R.color.categoryDefault, context.theme))
                    dividerColour.setBackgroundColor(resources.getColor(R.color.categoryDefault, context.theme))
                }
                else {
                    textViewCategory.text = note.category.name
                    constraintEndBorder.setBackgroundColor(note.category.colour)
                    dividerColour.setBackgroundColor(note.category.colour)
                    textViewCategory.visibility = View.VISIBLE
                }

                // Hide the title field and spacer if not set on the note
                if (note.title.isNullOrBlank()) {
                    textViewTitle.visibility = View.GONE
                    dividerWide.visibility = View.GONE
                } else {
                    textViewTitle.visibility = View.VISIBLE
                    dividerWide.visibility = View.VISIBLE
                }

                // Hide the date time if displaying in grid view
                if (NotesListViewStyleHandler.getNotesListViewStyle(context) == NotesListViewStyle.VIEW_STYLE_LIST) {
                    textViewDateTime.visibility = View.VISIBLE
                    imageViewClock.visibility = View.VISIBLE
                    textViewDateTime.text = formatDateTime(note.createdDateTime)
                } else {
                    textViewDateTime.visibility = View.INVISIBLE
                    imageViewClock.visibility = View.INVISIBLE
                }
            }
        }

        /**
         * Produce a concatenated version of the note title
         */
        private fun getDisplayTitle(title: String?): String? =
            if (title != null && title.length > 25) {
                title.take(25) + "..."
            } else {
                title
            }

        /**
         * Produce a concatenated version of the note content
         */
        private fun getDisplayContent(content: String): String {
            if (content.length > 150) {
                return content.take(150) + "..."
            }

            val linesAsList = content.lines()
            if (linesAsList.size > 10) {
                return linesAsList.take(10).joinToString("\r\n", "", "...", 10)
            }

            return content
        }
    }
}