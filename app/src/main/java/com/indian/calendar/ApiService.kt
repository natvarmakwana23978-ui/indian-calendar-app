package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // આખી લિંકને બદલે માત્ર 'macros/...' વાળો ભાગ જ લખવો
    @GET("macros/s/AKfycbzCMHnwTK5VT8p7-IV2EvmwaJKuW48A-vUmyk2Ymzcjq7w8GB7KcyxW6DOIAecCmuwM/exec")
    fun getCalendars(): Call<List<CalendarModel>>
}
