package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.model.CalendarDayData

class CalendarDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view) // અથવા તમારું બીજું લેઆઉટ

        val dayData = intent.getParcelableExtra<CalendarDayData>("day_data")
        val titleTxt = findViewById<TextView>(R.id.calendarTitleText)
        
        dayData?.let {
            titleTxt?.text = "${it.Date}\n${it.Gujarati}"
        }
    }
}
