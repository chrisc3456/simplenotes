package com.simplenotes.notes.data.local

import androidx.room.*

@Dao
interface NoteEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteEntity)

    @Update
    fun updateNote(note: NoteEntity)

    @Delete
    fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM NoteEntity")
    fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE categoryId = :categoryId")
    fun getNotesForCategory(categoryId: Int): List<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    fun getNoteFromId(id: Int): NoteEntity?
}