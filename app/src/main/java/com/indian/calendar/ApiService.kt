package com.indian.calendar
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("exec")
    fun getCalendarList(): Call<List<CalendarItem>>

    @GET("exec")
    fun getCalendarData(
        @Query("action") action: String = "getCalendarDays",
        @Query("colIndex") colIndex: Int
    ): Call<List<CalendarDayData>>
}
