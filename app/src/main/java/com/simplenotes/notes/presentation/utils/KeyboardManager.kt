package com.simplenotes.notes.presentation.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardManager {
    /**
     * Hide the input keyboard from view
     */
    fun hideKeyboard(activity: Activity, view: View) {
        val inputManager: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}