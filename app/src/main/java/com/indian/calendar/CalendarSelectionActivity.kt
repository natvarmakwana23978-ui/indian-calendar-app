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

    private var allCalendarData: List<JsonObject> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        fetchData()
    }

    private fun fetchData() {
        // ૧. Retrofit માં 'instance' ને બદલે સાચી રીતે કૉલ કરો
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        
        apiService.getCalendarData().enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    allCalendarData = response.body()!!
                    setupLanguageList()
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "ડેટા ખાલી છે", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupLanguageList() {
        // ૨. તમારી XML મુજબ સાચું ID: calendarSelectionRecyclerView [cite: 2026-01-25]
        val recyclerView = findViewById<RecyclerView>(R.id.calendarSelectionRecyclerView)
        
        // ભાષાઓનું લિસ્ટ
        val languages = listOf("ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "Islamic", "Punjabi", "Marathi")
        
        // ૩. એડેપ્ટર સેટઅપ
        // જો અહીં 'Type mismatch' આવે, તો CalendarSelectionAdapter નો કોડ ચેક કરવો પડશે
        val adapter = CalendarSelectionAdapter(languages) { selectedLang ->
            val gson = Gson()
            val jsonString: String = gson.toJson(allCalendarData)
            
            val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
            intent.putExtra("SELECTED_LANGUAGE", selectedLang)
            intent.putExtra("CALENDAR_DATA", jsonString)
            startActivity(intent)
        }
        
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
