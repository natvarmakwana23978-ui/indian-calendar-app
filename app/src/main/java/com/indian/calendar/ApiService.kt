package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // બધી ઉપલબ્ધ શીટ્સ (કેલેન્ડર) ના નામ મેળવવા માટે
    @GET("exec")
    fun getCalendarList(): Call<CalendarListResponse>

    // સિલેક્ટ કરેલા કેલેન્ડરનો ડેટા મેળવવા માટે (દા.ત. calendar=calendarfinaldata)
    @GET("exec")
    fun getCalendarData(
        @Query("calendar") calendarName: String
    ): Call<List<Map<String, String>>>
}

// સ્ક્રિપ્ટ જે લિસ્ટ મોકલે છે તેના માટેનું મોડલ
data class CalendarListResponse(
    val available_calendars: List<String>
)
