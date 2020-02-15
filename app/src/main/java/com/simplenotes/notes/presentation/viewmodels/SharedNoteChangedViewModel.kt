package com.simplenotes.notes.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedNotesChangedViewModel: ViewModel() {

    val noteChanged = MutableLiveData<Boolean>()

    fun noteUpdated() {
        noteChanged.value = true
    }
}