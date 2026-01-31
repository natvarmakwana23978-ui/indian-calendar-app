package com.indian.calendar

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("exec")
    fun getCalendarData(
        @Query("calendar") sheetName: String, // "Sheet1" અથવા "Sheet2"
        @Query("action") action: String       // "getDays" લોજિક માટે
    ): Call<List<JsonObject>>
}
