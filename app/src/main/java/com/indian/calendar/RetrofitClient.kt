package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // તમારી Apps Script ને Deploy કર્યા પછી મળતી લિંક અહીં 'exec' પહેલા સુધીની નાખો
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbzcqnaGd3uJ4-q4W0eFz7XUi0eymsSxjN1wapHDNTfNnJmjx-Yj2PECk1O7u8i9ULJy/exec/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
