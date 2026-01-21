package com.indian.calendar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        recyclerView = findViewById(R.id.calendarRecyclerView)
        // ૭ કોલમવાળી ગ્રીડ સેટ કરી
        recyclerView.layoutManager = GridLayoutManager(this, 7)

        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        loadCalendarData(colIndex)
    }

    private fun loadCalendarData(colIndex: Int) {
        RetrofitClient.api.getCalendarData(colIndex = colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                if (response.isSuccessful) {
                    val days = response.body() ?: emptyList()
                    // અહીં સુધારો કર્યો: CalendarViewAdapter ને બદલે CalendarDayAdapter વાપર્યું [cite: 2026-01-21]
                    recyclerView.adapter = CalendarDayAdapter(days)
                }
            }
            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                Toast.makeText(this@CalendarViewActivity, "ડેટા લોડ કરવામાં નિષ્ફળ", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
