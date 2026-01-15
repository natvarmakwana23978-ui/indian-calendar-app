package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // 🔹 Calendar list (Gujarati, Hindi, Islamic, etc.)
    @GET("exec")
    fun getCalendars(): Call<List<CalendarModel>>

    // 🔹 Selected calendar data (by column index)
    @GET("exec")
    fun getCalendarData(
        @Query("colIndex") colIndex: Int
    ): Call<List<CalendarDayData>>
}
