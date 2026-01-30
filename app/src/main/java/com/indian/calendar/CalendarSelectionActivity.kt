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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        val calSpinner = findViewById<Spinner>(R.id.calendarSpinner)
        val langSpinner = findViewById<Spinner>(R.id.languageSpinner)
        val btnOpen = findViewById<Button>(R.id.btnOpenCalendar)

        // ૧. કેલેન્ડરની યાદી (તમારા આઈડિયા મુજબ ભારતનાં વિવિધ કેલેન્ડર)
        val calendars = listOf("ગુજરાત પંચાંગ", "પંજાબી કેલેન્ડર", "મહારાષ્ટ્ર પંચાંગ", "તમિલ કેલેન્ડર")
        calSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)

        // ૨. ભાષાની યાદી
        val languages = listOf("ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "ENGLISH")
        langSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        btnOpen.setOnClickListener {
            val selectedLang = langSpinner.selectedItem.toString()
            val selectedCal = calSpinner.selectedItem.toString() // આ ભવિષ્યમાં શીટ બદલવા કામ લાગશે
            
            val pd = android.app.ProgressDialog(this).apply { setMessage("ડેટા લોડ થઈ રહ્યો છે..."); show() }

            apiService.getCalendarData("Sheet1").enqueue(object : Callback<List<JsonObject>> {
                override fun onResponse(call: Call<List<JsonObject>>, res1: Response<List<JsonObject>>) {
                    pd.dismiss()
                    if (res1.isSuccessful && res1.body() != null) {
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("DATA", Gson().toJson(res1.body()))
                        intent.putExtra("SELECTED_LANG", selectedLang)
                        intent.putExtra("CAL_TITLE", selectedCal)
                        startActivity(intent)
                    }
                }
                override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) { pd.dismiss() }
            })
        }
    }
}
