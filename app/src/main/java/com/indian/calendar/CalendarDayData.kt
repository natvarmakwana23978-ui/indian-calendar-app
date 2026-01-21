package com.indian.calendar
import com.google.gson.annotations.SerializedName

data class CalendarDayData(
    @SerializedName("ENGLISH") val englishDate: String? = null,
    @SerializedName("local_date") val localDate: String? = null,
    @SerializedName("Alert") val alert: String? = null
)
