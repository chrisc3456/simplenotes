package com.simplenotes.notes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NoteEntity::class, CategoryEntity::class], version = 1)
@TypeConverters(LocalDateTimeTypeConverter::class)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteEntityDao
    abstract fun categoryDao(): CategoryEntityDao

    /**
     * Companion object to provide static access for the creation and deletion of singleton database instance
     */
    companion object {
        private var database: NoteDatabase? = null

        /**
         * Build the database, applying necessary callbacks and migration paths for version changes
         */
        fun getDatabase(context: Context): NoteDatabase? {
            if (database == null) {
                synchronized(NoteDatabase::class) {
                    database = Room.databaseBuilder(context.applicationContext,
                        NoteDatabase::class.java, "notesdb")
                        .build()
                }
            }

            return database
        }
    }
}