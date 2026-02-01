package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class CalendarViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val fullJson = intent.getStringExtra("DATA") ?: ""
        val lang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"
        val calIndex = intent.getIntExtra("CAL_INDEX", 0)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = CalendarPagerAdapter(this, fullJson, lang, calIndex)
    }
}
