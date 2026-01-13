package com.indian.calendar

import com.google.gson.annotations.SerializedName

data class CalendarDayData(
    @SerializedName("date") val date: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("festival") val festival: String
)
