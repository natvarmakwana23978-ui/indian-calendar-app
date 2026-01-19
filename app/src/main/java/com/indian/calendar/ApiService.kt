package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("exec")
    fun getCalendars(@Query("action") action: String = "getCalendars"): Call<List<CalendarItem>>

    @GET("exec")
    fun getCalendarData(
        @Query("colIndex") colIndex: Int,
        @Query("action") action: String = "getCalendarData"
    ): Call<List<CalendarDayData>>
}
