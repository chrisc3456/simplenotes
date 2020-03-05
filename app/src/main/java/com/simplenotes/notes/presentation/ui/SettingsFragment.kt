package com.simplenotes.notes.presentation.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.simplenotes.notes.R
import com.simplenotes.notes.presentation.utils.DayNightModeHandler
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setupBackNavigationCallback()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val displayModePreference = findPreference<ListPreference>(getString(R.string.settings_key_display_mode))
        displayModePreference?.setOnPreferenceChangeListener { _, newValue -> DayNightModeHandler.updateDayNightMode(newValue.toString()) }
    }

    /**
     * Set up the toolbar and associate with the activity support action bar, including navigation controller support
     */
    private fun setupActionBar() {
        setHasOptionsMenu(true)

        val hostActivity = requireActivity() as AppCompatActivity
        hostActivity.setSupportActionBar(toolbarSettings)
        hostActivity.setupActionBarWithNavController(findNavController())

        val actionBar = hostActivity.supportActionBar
        actionBar?.title = resources.getString(R.string.title_settings)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(true)
    }

    /**
     * Set up a call back to save when the back navigation is triggered
     */
    private fun setupBackNavigationCallback() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
        callback.isEnabled = true
    }

    /**
     * Support pressing of the back button using navigation including any custom callbacks (see setupBackNavigationCallback)
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}