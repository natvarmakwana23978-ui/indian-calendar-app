package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // આખી લિંકને બદલે માત્ર 'macros/...' વાળો ભાગ જ લખવો
    @GET("macros/s/AKfycbzmQcQuVP_40GirpWWIVMT5XmhkgQbt6kz6bN8IBbToAmdCvXKz6Wz4dhk_fuoxq7fM/exec")
    fun getCalendars(): Call<List<CalendarModel>>
}
