package com.indian.calendar

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Data models
data class CalendarItem(val calendarName: String, val creatorName: String)
data class CalendarData(val englishDate: String, val tithi: String?, val festival: String?)

// Retrofit API
interface ApiService {
    @GET("exec")
    suspend fun getCalendars(): List<CalendarItem>

    @GET("exec")
    suspend fun getCalendarData(@Query("colIndex") colIndex: Int): List<CalendarData>
}

object RetrofitClient {
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbzsFfM_jo2P_PmCtDyccoC6KIubETZxjAnAtwLBTJRtidKIicS5cKf9l5KrMC9TDRWt/"

    private val okHttp = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
