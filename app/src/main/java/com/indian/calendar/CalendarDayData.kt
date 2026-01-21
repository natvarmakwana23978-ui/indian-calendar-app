package com.indian.calendar

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarDayData(
    @SerializedName("ENGLISH") 
    val Date: String = "", // 'd' ને બદલે 'D' [cite: 2026-01-21]
    
    @SerializedName("local_date") 
    val Gujarati: String = "", // 'localDate' ને બદલે 'Gujarati' [cite: 2026-01-21]
    
    @SerializedName("Alert") 
    val Alert: String = ""
) : Parcelable
