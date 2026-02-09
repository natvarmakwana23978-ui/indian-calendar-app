package com.smart.reminder
import com.google.gson.annotations.SerializedName

data class CalendarItem(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null
)
