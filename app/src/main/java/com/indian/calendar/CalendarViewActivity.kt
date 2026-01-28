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
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvMainHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        recyclerView = findViewById(R.id.calendarRecyclerView)
        tvMainHeader = findViewById(R.id.tvMainHeader)

        val jsonData = intent.getStringExtra("DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"
        
        // પસંદ કરેલા કેલેન્ડરનું નામ હેડરમાં બતાવો
        tvMainHeader.text = selectedLang

        recyclerView.layoutManager = GridLayoutManager(this, 7)

        if (!jsonData.isNullOrEmpty()) {
            val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
            val daysList = mutableListOf<CalendarDayData?>()

            // જાન્યુઆરીની શરૂઆત ગુરુવારથી કરવા માટે ૪ ખાલી ખાના
            for (i in 0 until 4) daysList.add(null)

            dataList.forEach { json ->
                val dateStr = json.get("ENGLISH")?.asString ?: ""
                daysList.add(CalendarDayData(dateStr, json))
            }
            recyclerView.adapter = CalendarAdapter(daysList, selectedLang)
        }
    }
}
