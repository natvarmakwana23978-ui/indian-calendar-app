package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {

    // આ લિસ્ટમાં આપણે ગૂગલ શીટનો ડેટા સ્ટોર કરીશું
    private var allCalendarData: List<JsonObject> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        // ૧. ગૂગલ શીટમાંથી ડેટા ખેંચો
        fetchData()
    }

    private fun fetchData() {
        RetrofitClient.instance.getCalendarData().enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    allCalendarData = response.body()!!
                    // ડેટા મળી જાય પછી લિસ્ટ બતાવો
                    setupLanguageList()
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "ડેટા ખાલી છે", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupLanguageList() {
        val recyclerView = findViewById<RecyclerView>(R.id.languageRecyclerView)
        
        // તમારી ગૂગલ શીટના હેડર્સ મુજબ લિસ્ટ (આમાં સ્પેલિંગ ભૂલ ન હોવી જોઈએ)
        val languages = listOf(
            "ગુજરાતી (Gujarati)",
            "हिन्दी (Hindi)",
            "Islamic",
            "Punjabi",
            "Marathi",
            "Telugu/Kannada"
        )
        
        // એડેપ્ટર સેટ કરો (ખાતરી કરો કે CalendarSelectionAdapter માં ક્લિક લિસનર છે)
        val adapter = CalendarSelectionAdapter(languages) { selectedLang ->
            // ૨. ક્લિક કરવા પર બીજી એક્ટિવિટી ખોલો
            val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
            
            // ડેટાને String માં ફેરવીને મોકલો જેથી એરર ન આવે [cite: 2026-01-23]
            val dataAsString: String = Gson().toJson(allCalendarData)
            
            intent.putExtra("SELECTED_LANGUAGE", selectedLang) 
            intent.putExtra("CALENDAR_DATA", dataAsString)
            
            startActivity(intent)
        }
        
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
