package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // તમારી Apps Script ને 'Deploy' કર્યા પછી મળેલી લિંક અહીં 'exec' સુધી નાખો
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbxDIcaJQ85VDhuup3Qn2YUCpnhX1OtDDTEIgMkTyqVeKHGmSBlh2-hukwhU6L58r1UO/exec/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
