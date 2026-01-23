package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.indian.calendar.R

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var tvMonthYearLabel: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // XML ના IDs સાથે કનેક્શન [cite: 2026-01-23]
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        tvMonthYearLabel = findViewById(R.id.tvMonthYearLabel)
        progressBar = findViewById(R.id.progressBar)

        // કેલેન્ડર ગ્રીડ માટે ૭ કોલમ સેટ કરો (રવિ થી શનિ) [cite: 2026-01-22]
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
    }

    fun setupCalendarData(responseBody: List<JsonObject>, selectedHeader: String, monthTitle: String) {
        tvMonthYearLabel.text = monthTitle
        
        val daysList = responseBody.map { 
            CalendarDayData(it.get("ENGLISH")?.asString ?: "", it) 
        }

        // એડેપ્ટર સેટ કરો
        val adapter = CalendarAdapter(daysList, selectedHeader)
        calendarRecyclerView.adapter = adapter
    }
}
