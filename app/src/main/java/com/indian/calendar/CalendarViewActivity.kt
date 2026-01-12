package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var tvMonthYear: TextView
    private lateinit var calendarViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        tvMonthYear = findViewById(R.id.tvMonthYear)
        calendarViewPager = findViewById(R.id.calendarViewPager)

        val startCalendar = Calendar.getInstance()
        val adapter = MonthPagerAdapter(startCalendar)
        calendarViewPager.adapter = adapter

        calendarViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, position - 500)
                val sdf = SimpleDateFormat("MMMM yyyy", Locale("gu"))
                tvMonthYear.text = sdf.format(calendar.time)
            }
        })
        calendarViewPager.setCurrentItem(500, false)
    }
}
