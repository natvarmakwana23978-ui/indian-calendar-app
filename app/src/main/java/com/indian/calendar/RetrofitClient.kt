package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // તમારી નવી લિંક અહીં સેટ કરી છે [cite: 2026-01-14, 2026-01-21]
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbxIaJLy_PMHM7RrhCcJn_bd3J8lRJ6C9irY-TCfZnNtL-CzcDdiNfSoWjOimKGp5v9u/" 

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
