package com.indian.calendar

import com.indian.calendar.model.CalendarDayData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // તમારી ગૂગલ શીટની સ્ક્રિપ્ટ મુજબનો એન્ડપોઈન્ટ
    @GET("exec") 
    fun getCalendarDetails(
        @Query("action") action: String = "getCalendarData",
        @Query("colIndex") colIndex: Int
    ): Call<List<CalendarDayData>>
}
