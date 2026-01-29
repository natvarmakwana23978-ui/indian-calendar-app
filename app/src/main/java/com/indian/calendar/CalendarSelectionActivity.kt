package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        apiService = RetrofitClient.instance.create(ApiService::class.java)
        val spinner = findViewById<Spinner>(R.id.languageSpinner)
        val btnOpen = findViewById<Button>(R.id.btnOpenCalendar)

        val languages = listOf("ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "ENGLISH")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        btnOpen.setOnClickListener {
            val selectedLang = spinner.selectedItem.toString()
            fetchBothSheets(selectedLang)
        }
    }

    private fun fetchBothSheets(lang: String) {
        apiService.getCalendarData("Sheet1").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, res1: Response<List<JsonObject>>) {
                val s1 = Gson().toJson(res1.body())
                apiService.getCalendarData("Sheet2").enqueue(object : Callback<List<JsonObject>> {
                    override fun onResponse(call: Call<List<JsonObject>>, res2: Response<List<JsonObject>>) {
                        val s2 = Gson().toJson(res2.body())
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("DATA", s1)
                        intent.putExtra("WEEKDAYS_DATA", s2)
                        intent.putExtra("SELECTED_LANG", lang)
                        startActivity(intent)
                    }
                    override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {}
                })
            }
            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {}
        })
    }
}
