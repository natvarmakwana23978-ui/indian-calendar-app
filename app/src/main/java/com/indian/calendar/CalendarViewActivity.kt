package com.indian.calendar

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class CalendarViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvHeader: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // XML ના આઈડી સાથે જોડાણ
        recyclerView = findViewById(R.id.calendarRecyclerView)
        tvHeader = findViewById(R.id.tvMonthYearLabel)
        progressBar = findViewById(R.id.progressBar)

        // તમારા પ્લાન મુજબ નીચેથી ઉપર સ્ક્રોલ કરવા માટે (Vertical Scrolling)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val jsonData = intent.getStringExtra("DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"

        if (!jsonData.isNullOrEmpty()) {
            try {
                // ગૂગલ શીટનો ડેટા JSON માંથી લિસ્ટમાં રૂપાંતરિત કરો
                val dataList: List<JsonObject> = Gson().fromJson(
                    jsonData, object : TypeToken<List<JsonObject>>() {}.type
                )

                val daysList = mutableListOf<CalendarDayData?>()

                // પગલું ૫: ૧ જાન્યુઆરી ૨૦૨૬ એ ગુરુવાર છે.
                // રવિવાર થી શરૂ થતા ગ્રીડમાં ગુરુવાર ૫માં સ્થાને આવે (Index 4)
                // તેથી ૪ ખાલી (null) ખાના ઉમેરવા પડશે.
                for (i in 1..4) {
                    daysList.add(null)
                }

                // શીટના ડેટાને એડેપ્ટરના ફોર્મેટ (CalendarDayData) માં ફેરવો
                dataList.forEach { json ->
                    val dateStr = json.get("ENGLISH")?.asString ?: ""
                    daysList.add(CalendarDayData(dateStr, json))
                }

                // હેડરમાં વર્ષ બતાવો
                tvHeader.text = "કેલેન્ડર ૨૦૨૬"
                
                // એડેપ્ટર સેટ કરો જે કલર કોડિંગ અને ઇસ્લામિક ડેટા હેન્ડલ કરશે
                recyclerView.adapter = CalendarAdapter(daysList, selectedLang)

            } catch (e: Exception) {
                Toast.makeText(this, "ડેટા પ્રોસેસિંગમાં ભૂલ: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "ડેટા મળ્યો નથી", Toast.LENGTH_SHORT).show()
        }
    }
}
