package com.indian.calendar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarDayData(
    val Date: String = "",           // 'date' ને બદલે 'Date' (D કેપિટલ)
    val Day: String = "",            // 'dayName' ને બદલે 'Day'
    val Gujarati_Month: String = "", // નવો વેરીએબલ
    val Tithi: String = "",          // 'localDate' ને બદલે 'Tithi'
    val Festival_English: String = "", // 'festival' ને બદલે 'Festival_English'
    val emoji: String = ""
) : Parcelable
