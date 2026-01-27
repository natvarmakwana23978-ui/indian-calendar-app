package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var tvMonthYearLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        tvMonthYearLabel = findViewById(R.id.tvMonthYearLabel)
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)

        val jsonData = intent.getStringExtra("CALENDAR_DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANGUAGE") ?: "ગુજરાતી (Gujarati)"

        if (!jsonData.isNullOrEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<List<JsonObject>>() {}.type
            val dataList: List<JsonObject> = gson.fromJson(jsonData, type)
            
            val daysList = dataList.map { json ->
                CalendarDayData(json.get("ENGLISH")?.asString ?: "", json)
            }
            
            // તમારી શીટ મુજબનું લેબલ
            tvMonthYearLabel.text = "કેલેન્ડર ૨૦૨૬" 
            calendarRecyclerView.adapter = CalendarAdapter(daysList, selectedLang)
        }
    }
}
