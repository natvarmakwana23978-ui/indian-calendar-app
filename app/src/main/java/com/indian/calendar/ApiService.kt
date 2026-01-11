package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // આખી લિંકને બદલે માત્ર 'macros/...' વાળો ભાગ જ લખવો
    @GET("macros/s/AKfycbwzQhxKbkUkbiUTll6IF0vKv0-rvYvBdELRU1hRElWgegHq1g2-7SZiHNU_KNrUVFiG/exec")
    fun getCalendars(): Call<List<CalendarModel>>
}
