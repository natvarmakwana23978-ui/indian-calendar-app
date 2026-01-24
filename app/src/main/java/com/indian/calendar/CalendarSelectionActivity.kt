package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {

    private var allCalendarData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection) // તમારી ફાઈલ મુજબ નામ ચેક કરી લેવું

        // ૧. ડેટા ફેચ કરો
        fetchData()
    }

    private fun fetchData() {
        RetrofitClient.instance.getCalendarData().enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful) {
                    allCalendarData = Gson().toJson(response.body())
                    setupLanguageList()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                // એરર હેન્ડલિંગ
            }
        })
    }

    private fun setupLanguageList() {
        val recyclerView = findViewById<RecyclerView>(R.id.languageRecyclerView)
        val languages = listOf("ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "Islamic", "Punjabi") // તમારી શીટ મુજબ
        
        val adapter = CalendarSelectionAdapter(languages) { selectedLang ->
            // ૨. ક્લિક કરવા પર ડેટા પાસ કરો [cite: 2026-01-23]
            val intent = Intent(this, CalendarViewActivity::class.java)
            intent.putExtra("CALENDAR_DATA", allCalendarData)
            intent.putExtra("SELECTED_LANGUAGE", selectedLang)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
