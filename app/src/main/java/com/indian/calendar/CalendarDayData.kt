package com.indian.calendar.model

data class CalendarDayData(
    val date: String = "",       // Column A (અંગ્રેજી તારીખ)
    val dayName: String = "",    // Column (વાર)
    val localDate: String = "",  // તમારી પસંદ કરેલી ભાષાની તિથિ
    val festival: String = "",   // તહેવાર
    val emoji: String = ""       // ઈમોજી
)
