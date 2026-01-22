package com.indian.calendar

import com.google.gson.JsonObject

data class CalendarDayData(
    val englishDate: String,  // કોલમ A (ENGLISH)
    val allData: JsonObject   // આખા ૨૭ કોલમનો ડેટા (કોલમ B થી AA સુધી બધું જ) [cite: 2026-01-22]
)
