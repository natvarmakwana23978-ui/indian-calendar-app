package com.indian.calendar

import com.google.gson.annotations.SerializedName

data class CalendarModel(
    @SerializedName("calendarName") val name: String,
    @SerializedName("index") val index: Int
)
