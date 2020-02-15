package com.simplenotes.notes.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.simplenotes.notes.R
import com.simplenotes.notes.presentation.utils.DayNightModeHandler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getDayNightModePreference()
    }

    /**
     * Get the current preference value for day/night mode
     */
    private fun getDayNightModePreference() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val displayModePreference = sharedPreferences.getString(getString(R.string.settings_key_display_mode), null)
        DayNightModeHandler.updateDayNightMode(displayModePreference.toString())
    }
}
