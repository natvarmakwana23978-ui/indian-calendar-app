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
        val tvMonthYear = findViewById<TextView>(R.id.tvMonthYear)
        
        val jsonData = intent.getStringExtra("DATA")
        val lang = intent.getStringExtra("SELECTED_LANG") ?: "ENGLISH"
        val dayNamesJson = intent.getStringExtra("DAY_NAMES") // Sheet2 નો ડેટા

        // ૧. ડાયનેમિક હેડર સેટિંગ (બીજી પટ્ટી માટે)
        if (!dayNamesJson.isNullOrEmpty()) {
            val dayNames: List<String> = Gson().fromJson(dayNamesJson, object : TypeToken<List<String>>() {}.type)
            setupDynamicDayHeaders(dayNames)
        }

        tvMonthYear.text = "January - 2026" 

        rv.layoutManager = GridLayoutManager(this, 7)
        val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
        
        val finalItems = mutableListOf<CalendarDayData>()
        dataList.forEach { 
            finalItems.add(CalendarDayData(it.get("ENGLISH")?.asString ?: "", it)) 
        }
        
        rv.adapter = CalendarAdapter(finalItems, lang)
    }

    private fun setupDynamicDayHeaders(dayNames: List<String>) {
        val headerIds = listOf(R.id.tvDay1, R.id.tvDay2, R.id.tvDay3, R.id.tvDay4, R.id.tvDay5, R.id.tvDay6, R.id.tvDay7)
        for (i in 0 until dayNames.size.coerceAtMost(7)) {
            val tv = findViewById<TextView>(headerIds[i])
            tv.text = dayNames[i]
        }
    }
}
