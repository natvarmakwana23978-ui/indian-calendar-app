package com.indian.calendar

import androidx.room.*

@Dao
interface UserNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: UserNote)

    @Query("SELECT * FROM user_notes")
    suspend fun getAllNotes(): List<UserNote>

    // વિજેટ માટે તારીખ મુજબ નોટ લાવવાનું ફંક્શન
    @Query("SELECT * FROM user_notes WHERE date = :dateLimit LIMIT 1")
    suspend fun getNoteByDate(dateLimit: String): UserNote?

    @Delete
    suspend fun deleteNote(note: UserNote)
}
