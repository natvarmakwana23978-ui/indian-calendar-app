package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // લિસ્ટ મેળવવા માટે (અહીં ફક્ત "exec" લખવું કારણ કે બાકીની લિંક RetrofitClient માં છે)
    @GET("exec")
    fun getCalendars(): Call<List<CalendarModel>>

    // પસંદ કરેલ કેલેન્ડરનો ડેટા મેળવવા માટે
    @GET("exec")
    fun getCalendarData(@Query("colIndex") colIndex: Int): Call<List<CalendarDayData>>
}
