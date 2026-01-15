package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.model.CalendarDayData
import java.util.Calendar

data class CalendarData(val title: String)

class CalendarViewActivity : AppCompatActivity() {

    // Example: Map from CSV / database: "YYYY-M-D" -> CalendarDayData
    private val calendarMap: Map<String, CalendarDayData> = mapOf(
        "2026-1-15" to CalendarDayData(festival = "Pongal", reminder = "Meeting at 10AM"),
        "2026-1-16" to CalendarDayData(festival = "Makar Sankranti")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val calendar = Calendar.getInstance()
        val todayDataList: List<CalendarData> = getCalendarDataFor(calendar)

        todayDataList.forEach { println(it.title) }
    }

    private fun getCalendarDataFor(calendar: Calendar): List<CalendarData> {
        val key = "${calendar.get(Calendar.YEAR)}-" +
                  "${calendar.get(Calendar.MONTH) + 1}-" +
                  "${calendar.get(Calendar.DAY_OF_MONTH)}"

        return calendarMap[key]?.let { dayData ->
            val list = mutableListOf<CalendarData>()
            dayData.festival?.let { list.add(CalendarData("Festival: $it")) }
            dayData.reminder?.let { list.add(CalendarData("Reminder: $it")) }
            list
        } ?: emptyList()
    }
}
