package com.indian.calendar

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private var selectedDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        monthYearText = findViewById(R.id.monthYearTV)
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)

        setMonthView()
    }

    private fun setMonthView() {
        // પસંદ કરેલી ભાષા મેળવો (Default: gu)
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val lang = sharedPref.getString("selected_lang", "gu") ?: "gu"
        val locale = Locale(lang)

        // મહિનાનું નામ અને વર્ષ સેટ કરો
        val sdf = SimpleDateFormat("MMMM yyyy", locale)
        monthYearText.text = sdf.format(selectedDate.time)

        // તે મહિનાની તારીખો ગણવી
        val daysInMonth = getDaysInMonthArray()

        // ગ્રીડ સેટ કરો (૭ કોલમ - ૭ વાર માટે)
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
        calendarRecyclerView.adapter = CalendarAdapter(daysInMonth)
    }

    private fun getDaysInMonthArray(): ArrayList<String> {
        val daysList = ArrayList<String>()
        val monthCalendar = selectedDate.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        
        val firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1
        val daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // મહિનાની શરૂઆત પહેલા ખાલી જગ્યા છોડવી
        for (i in 1..firstDayOfWeek) {
            daysList.add("")
        }

        // તારીખો ઉમેરવી (૧ થી ૩૧)
        for (i in 1..daysInMonth) {
            daysList.add(i.toString())
        }
        return daysList
    }
}
