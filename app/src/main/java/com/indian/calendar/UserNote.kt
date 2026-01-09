package com.indian.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_notes")
data class UserNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,    // આ તારીખ વિજેટ માટે વપરાશે
    val note: String,
    val isReligious: Boolean = false
)
