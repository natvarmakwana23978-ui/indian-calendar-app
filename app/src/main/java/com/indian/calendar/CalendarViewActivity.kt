package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.indian.calendar.R

class CalendarViewActivity : AppCompatActivity() {

    // રિકાયકલર વ્યુને અહીં ડિક્લેર કરો જેથી 'Unresolved reference' એરર ન આવે
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // XML માંથી ID મેળવો
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // એડેપ્ટર સેટ કરવા માટેની મેથડ
    fun setupCalendar(monthData: List<JsonObject>, selectedHeader: String) {
        val adapter = MonthAdapter(monthData, selectedHeader)
        recyclerView.adapter = adapter
    }
}
