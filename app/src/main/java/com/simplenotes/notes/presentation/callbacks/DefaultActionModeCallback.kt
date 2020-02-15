package com.simplenotes.notes.presentation.callbacks

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes

class DefaultActionModeCallback(private val onActionItemClickListener: OnActionItemClickListener?): ActionMode.Callback {

    private var mode: ActionMode? = null
    @MenuRes private var menuResId: Int = 0
    private var title: String? = null
    private var subtitle: String? = null
    private var view: View? = null

    /**
     * Called when the action mode is created; startActionMode() was called
     */
    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        this.mode = mode
        mode.menuInflater.inflate(menuResId, menu)
        mode.title = title
        mode.subtitle = subtitle
        return true
    }

    /**
     * Called each time the action mode is shown. Always called after onCreateActionMode, but may be called multiple times if the mode is invalidated
     */
    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    /**
     * Called when the user exits the action mode
     */
    override fun onDestroyActionMode(mode: ActionMode) {
        this.mode = null
    }

    /**
     * Called when the user selects a contextual menu item
     */
    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        onActionItemClickListener?.onActionItemClick(item)
        mode.finish()
        return true
    }

    fun startActionMode(view: View, @MenuRes menuResId: Int, title: String? = null, subtitle: String? = null) {
        this.view = view
        this.menuResId = menuResId
        this.title = title
        this.subtitle = subtitle
        view.startActionMode(this)
    }

    fun finishActionMode() {
        mode?.finish()
    }
}