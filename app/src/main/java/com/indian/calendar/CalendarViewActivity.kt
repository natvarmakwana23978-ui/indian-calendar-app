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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val rv = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        rv.layoutManager = GridLayoutManager(this, 7) // ૭ કોલમ [cite: 2026-01-23]

        // ડેટા મેળવો
        val jsonData = intent.getStringExtra("CALENDAR_DATA")
        val lang = intent.getStringExtra("SELECTED_LANGUAGE") ?: "ENGLISH"

        if (jsonData != null) {
            val type = object : TypeToken<List<JsonObject>>() {}.type
            val dataList: List<JsonObject> = Gson().fromJson(jsonData, type)
            
            val days = dataList.map { CalendarDayData(it.get("ENGLISH")?.asString ?: "", it) }
            rv.adapter = CalendarAdapter(days, lang)
        }
    }
}
