package com.indian.calendar

import com.google.gson.annotations.SerializedName

data class CalendarItem(
    @SerializedName("id", alternate = ["ID", "Id"]) 
    val id: String? = null,
    
    @SerializedName("name", alternate = ["Name", "NAME", "calendar_name"]) 
    val name: String? = null
)
