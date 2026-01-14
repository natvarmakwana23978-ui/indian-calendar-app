package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // તમારી નવી લિંક
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycby0axtqyL6yjPOsHjPtKYBdQtp5C6IbJADsh5QA22ADS8Z9hi5HY2y17dLUK3t0VOob/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // અહીં ::class.java વાપરવું ફરજિયાત છે
        retrofit.create(ApiService::class.java)
    }
}
