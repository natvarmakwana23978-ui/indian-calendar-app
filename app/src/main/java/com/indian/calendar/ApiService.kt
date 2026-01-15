package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Calendar List (Gujarati, Hindi, etc.)
    @GET("exec")
    fun getCalendars(): Call<List<CalendarModel>>

    // Calendar Date-wise Data
    @GET("exec")
    fun getCalendarData(
        @Query("colIndex") colIndex: Int
    ): Call<List<CalendarDayData>>
}
