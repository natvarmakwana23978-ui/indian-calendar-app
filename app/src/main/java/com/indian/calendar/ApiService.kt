package com.indian.calendar

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("exec")
    suspend fun getCalendars(): List<CalendarModel>

    @GET("exec")
    suspend fun getCalendarData(
        @Query("colIndex") colIndex: Int
    ): List<CalendarDayData>
}
