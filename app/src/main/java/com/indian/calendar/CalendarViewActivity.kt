package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var tvMonthYear: TextView
    private lateinit var rvCalendar: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        tvMonthYear = findViewById(R.id.tvMonthYear)
        rvCalendar = findViewById(R.id.rvCalendar)

        setupCalendar()
    }

    private fun setupCalendar() {
        // અહીં ડેટા લાવવાનું અને એડેપ્ટર સેટ કરવાનું કામ થશે
        tvMonthYear.text = "જાન્યુઆરી ૨૦૨૬"
        
        val dummyDays = (1..31).map { it.toString() }
        val dummyMonths = listOf(dummyDays) // એક મહિનાનો ડેટા
        
        val pagerAdapter = MonthPagerAdapter(dummyMonths)
        rvCalendar.layoutManager = LinearLayoutManager(this)
        rvCalendar.adapter = pagerAdapter
    }
}
