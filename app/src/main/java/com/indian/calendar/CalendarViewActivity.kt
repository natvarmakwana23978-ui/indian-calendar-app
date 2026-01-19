package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
// model ઈમ્પોર્ટ હટાવી દીધો છે
import retrofit2.*

class CalendarViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var monthSelectionLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        recyclerView = findViewById(R.id.calendarRecyclerView)
        monthSelectionLayout = findViewById(R.id.monthSelectionLayout)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupMonthButtons()
        
        // જો ઇન્ટેન્ટમાં ડેટા ન હોય તો ડિફોલ્ટ ૧ નંબર (ગુજરાતી) લોડ થશે
        loadData(intent.getIntExtra("COL_INDEX", 1))
    }

    private fun setupMonthButtons() {
        for (i in 1..12) {
            val btn = Button(this).apply {
                text = i.toString()
                // મહિના મુજબ સ્ક્રોલ કરવાનું લોજિક
                setOnClickListener { 
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset((i - 1) * 31, 0) 
                }
            }
            monthSelectionLayout.addView(btn)
        }
    }

    private fun loadData(colIndex: Int) {
        RetrofitClient.api.getCalendarData(colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = CalendarDayAdapter(response.body() ?: emptyList(), colIndex)
                }
            }
            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                Toast.makeText(this@CalendarViewActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
