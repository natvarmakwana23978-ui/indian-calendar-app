package com.indian.calendar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarDayData(
    val festival: String? = null,
    val reminder: String? = null
) : Parcelable
