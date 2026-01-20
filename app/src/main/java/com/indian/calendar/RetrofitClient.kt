package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL માંથી 'exec' કાઢી નાખ્યું છે અને અંતમાં '/' રાખ્યો છે જે જરૂરી છે [cite: 2026-01-14]
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbzHOrbkFzwuFut-nIKgodBGji_DLE0LQda3qeH5Ml0oVXdQdOg18tUG9CtmCX8nBYF7/" 

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
