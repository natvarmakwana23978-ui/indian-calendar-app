package com.indian.calendar

import androidx.room.*

@Dao
interface UserNoteDao {
    // કોઈ ચોક્કસ તારીખ માટે નોંધ મેળવવા માટે
    @Query("SELECT * FROM user_notes WHERE dateKey = :date")
    suspend fun getNoteByDate(date: String): UserNote?

    // નવી નોંધ ઉમેરવા અથવા જૂની નોંધ અપડેટ કરવા માટે
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: UserNote)

    // ૩૬૫ દિવસના લિસ્ટ માટે બધી જ નોંધો મેળવવા
    @Query("SELECT * FROM user_notes")
    suspend fun getAllNotes(): List<UserNote>

    // કોઈ નોંધ ડીલીટ કરવા માટે (જો જરૂર પડે તો)
    @Delete
    suspend fun deleteNote(note: UserNote)
}

