package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // મુખ્ય કેલેન્ડર લિસ્ટ મેળવવા માટે [cite: 2026-01-20]
    @GET("exec")
    fun getCalendars(): Call<List<CalendarItem>>

    // કેલેન્ડરની અંદરનો ડેટા (તારીખ-તિથિ) મેળવવા માટે [cite: 2026-01-20]
    @GET("exec")
    fun getCalendarData(
        @Query("action") action: String = "getCalendarDays", // સ્ક્રિપ્ટ મુજબનું સાચું નામ [cite: 2026-01-20]
        @Query("colIndex") colIndex: Int
    ): Call<List<CalendarDayData>>
}
