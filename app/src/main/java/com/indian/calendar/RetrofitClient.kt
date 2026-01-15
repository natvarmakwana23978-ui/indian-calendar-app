package com.indian.calendar

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // 👇 અહીં ફક્ત EXEC URL સુધીનો ભાગ
    private const val BASE_URL =
        "https://script.google.com/macros/s/AKfycbEBQpPWdz_7SjzjghU4IaMBIaR98LOrop8qeGhBXmlfog028zg6TfdujX0RQzehUDH/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
