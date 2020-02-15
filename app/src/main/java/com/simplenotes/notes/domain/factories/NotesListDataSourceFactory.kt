package com.simplenotes.notes.domain.factories

import android.app.Application
import com.simplenotes.notes.data.repositories.DataSourceContext
import com.simplenotes.notes.data.repositories.NotesListRepository
import com.simplenotes.notes.domain.repositories.NotesListDataSource

class NotesListDataSourceFactory {

    /**
     * Create the appropriate repository without exposing details of the implementation to references
     */
    companion object {
        @JvmStatic
        fun createDataSource(application: Application): NotesListDataSource = NotesListRepository(
            DataSourceContext(application)
        )
    }
}