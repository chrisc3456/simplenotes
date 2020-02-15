package com.simplenotes.notes.domain.factories

import android.app.Application
import com.simplenotes.notes.data.repositories.DataSourceContext
import com.simplenotes.notes.data.repositories.NoteUpdateRepository
import com.simplenotes.notes.domain.repositories.NoteUpdateDataSource

class NoteUpdateDataSourceFactory {

    /**
     * Create the appropriate repository without exposing details of the implementation to references
     */
    companion object {
        @JvmStatic
        fun createDataSource(application: Application): NoteUpdateDataSource = NoteUpdateRepository(
            DataSourceContext(application)
        )
    }
}