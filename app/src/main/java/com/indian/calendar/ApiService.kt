package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // આખી લિંકને બદલે માત્ર 'macros/...' વાળો ભાગ જ લખવો
    @GET("macros/s/AKfycbwAh81n1ZPfh5vf9CBm0BX7ToditvV4L9auasPwwBSdgkOjoW4O4ywB3f1cclX6LQN6/exec")
    fun getCalendars(): Call<List<CalendarModel>>
}
