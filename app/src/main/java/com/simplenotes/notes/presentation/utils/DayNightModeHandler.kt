package com.simplenotes.notes.presentation.utils

import androidx.appcompat.app.AppCompatDelegate

object DayNightModeHandler {

    /**
     * Update the app day/night mode when the relevant preference is changed
     */
    fun updateDayNightMode(currentValue: String): Boolean {
        if (currentValue == "Dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        return true
    }

}