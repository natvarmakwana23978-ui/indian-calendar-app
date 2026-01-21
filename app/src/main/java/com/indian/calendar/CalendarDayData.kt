package com.indian.calendar

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarDayData(
    // ગૂગલ સ્ક્રિપ્ટ આ ૩ નામથી જ ડેટા મોકલે છે [cite: 2026-01-21]
    @SerializedName("ENGLISH") 
    val date: String = "",
    
    @SerializedName("local_date") 
    val localDate: String = "", // આ એક જ લાઈન બધી ભાષાઓ (ગુજરાતી, હિન્દી વગેરે) માટે કામ કરશે [cite: 2026-01-21]
    
    @SerializedName("Alert") 
    val alert: String = ""
) : Parcelable
