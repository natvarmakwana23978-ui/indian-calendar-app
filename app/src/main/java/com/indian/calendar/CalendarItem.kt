package com.indian.calendar

import com.google.gson.annotations.SerializedName

data class CalendarItem(
    // JSON માં જે નામ હોય તે જ અહીં લખવું પડે [cite: 2026-01-20, 2026-01-21]
    @SerializedName("id") 
    val id: String? = null,
    
    @SerializedName("name") 
    val name: String? = null
)
