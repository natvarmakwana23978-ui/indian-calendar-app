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

        // ૧. સૌથી પહેલા ગૂગલ શીટમાંથી ડેટા લોડ કરો. 
        // અહીં મેં તમારી મુખ્ય શીટનું નામ "calendarfinaldata" લખ્યું છે.
        fetchData("calendarfinaldata")
    }

    private fun fetchData(sheetName: String) {
        // RetrofitClient.api નો ઉપયોગ કરીને ડેટા મંગાવો
        RetrofitClient.api.getCalendarData(sheetName).enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    allCalendarData = response.body()!!
                    setupLanguageList()
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "ડેટા લોડ કરવામાં ભૂલ આવી", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupLanguageList() {
        val recyclerView = findViewById<RecyclerView>(R.id.calendarSelectionRecyclerView)
        
        // તમારી ગૂગલ શીટમાં જે હેડર છે તે જ નામ અહીં લખવા
        val languages = listOf("ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "إسلامي (Islamic)", "ENGLISH")
        
        // એડેપ્ટરમાં ડેટા પાસ કરો
        val adapter = CalendarSelectionAdapter(languages) { selectedLang ->
            if (allCalendarData.isNotEmpty()) {
                val gson = Gson()
                // ૨. ડેટાને String માં ફેરવો જેથી Intent માં મોકલી શકાય
                val jsonString = gson.toJson(allCalendarData)
                
                val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                intent.putExtra("SELECTED_LANGUAGE", selectedLang)
                intent.putExtra("CALENDAR_DATA", jsonString)
                startActivity(intent)
            } else {
                Toast.makeText(this@CalendarSelectionActivity, "ડેટા હજુ લોડ થઈ રહ્યો છે...", Toast.LENGTH_SHORT).show()
            }
        }
        
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
