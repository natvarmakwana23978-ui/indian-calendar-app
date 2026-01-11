package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("macros/s/AKfycbxoURGUvXz4bVzQrfK5i4Db4BnJK35ab7vjYyNPhEBjDxBjGJg5afbR8MUQ3WMgXqQYOw/exec")
    fun getCalendars(): Call<List<CalendarModel>>
}
