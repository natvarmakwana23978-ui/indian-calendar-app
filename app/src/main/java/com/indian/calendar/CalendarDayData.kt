package com.indian.calendar

import com.google.gson.JsonObject

data class CalendarDayData(
    val englishDate: String,
    val allData: JsonObject,
    var colorCode: Int = 0, // 0=Normal, 1=Red(Weekend), 2=Orange, 3=Green, 4=Blue
    val isEmpty: Boolean = false,
    
    // New fields for widget
    val isWeekend: Boolean = false,
    val hasReminders: Boolean = false
)
