package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // આખી લિંક અહીં જ આપી દીધી છે
    @GET("macros/s/AKfycbEBQpPWdz_7SjzjghU4IaMBIaR98LOrop8qeGhBXmlfog028zg6TfdujX0RQzehUDH/exec")
    fun getCalendars(): Call<List<CalendarModel>>

    @GET("macros/s/AKfycbEBQpPWdz_7SjzjghU4IaMBIaR98LOrop8qeGhBXmlfog028zg6TfdujX0RQzehUDH/exec")
    fun getCalendarData(@Query("colIndex") colIndex: Int): Call<List<CalendarDayData>>
}
