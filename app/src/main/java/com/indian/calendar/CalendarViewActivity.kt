package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val adapter = MonthPagerAdapter(
            context = this,
            startCalendar = Calendar.getInstance()
        ) { calendar ->

            val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
            val keyMonth = sdf.format(calendar.time)

            sheetDataMap
                .filterKeys { it.startsWith(keyMonth) }
                .map { entry ->
                    CalendarDayData(
                        date = entry.key,
                        tithi = entry.value.tithi,
                        festival = entry.value.festival,
                        note = entry.value.note
                    )
                }
        }

        viewPager.adapter = adapter
        viewPager.setCurrentItem(500, false)
    }
}
