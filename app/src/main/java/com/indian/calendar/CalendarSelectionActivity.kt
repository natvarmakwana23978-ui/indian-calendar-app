package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
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

        // XML માંથી બટન અને સ્પિનર મેળવો
        val btnNext = findViewById<Button>(R.id.btnNext) 
        val spinnerLang = findViewById<Spinner>(R.id.spinnerLanguage)

        // બટન પર ક્લિક લિસનર
        btnNext.setOnClickListener {
            val selectedLanguage = spinnerLang.selectedItem.toString()
            
            // ટેસ્ટ કરવા માટે ટોસ્ટ મેસેજ
            Toast.makeText(this, "પસંદ કરેલ ભાષા: $selectedLanguage", Toast.LENGTH_SHORT).show()
            
            // API માંથી ડેટા લાવવાનું શરૂ કરો
            loadCalendarAndDays("Sheet1", selectedLanguage)
        }
    }

    private fun loadCalendarAndDays(sheetName: String, selectedLang: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        apiService.getCalendarData(sheetName, "getData").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    val calendarData = Gson().toJson(response.body())
                    
                    // ડેટા મળી જાય એટલે બીજી એક્ટિવિટી પર જાઓ
                    val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                    intent.putExtra("DATA", calendarData)
                    intent.putExtra("SELECTED_LANG", selectedLang)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "સર્વરથી ડેટા મળ્યો નથી!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક ભૂલ: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
