package com.simplenotes.notes.presentation.callbacks

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class NoteSwipeToDeleteBasicCallback: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END + ItemTouchHelper.START) {

    /**
     * This defines whether items can be moved in the list, which we don't want to support in this case so just return false
     */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
}