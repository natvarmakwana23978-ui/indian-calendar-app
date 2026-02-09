package com.smart.reminder

import com.google.gson.JsonObject

data class CalendarDayData(
    val date: String,          // તારીખ
    val color_code: Int,       // ૧ એટલે રજા
    val details: JsonObject? = null, // ૨૭ કેલેન્ડરની વિગતો
    val isSunday: Boolean = false
)
