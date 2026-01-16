package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData
import org.json.JSONArray
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerCalendar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val todayData = getTodayCalendarData()
        recyclerView.adapter = CalendarAdapter(listOf(todayData))
    }

    private fun getTodayCalendarData(): CalendarDayData {
        val jsonStream: InputStream = assets.open("calendar.json")
        val jsonText = jsonStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonText)

        val today = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val todayStr = sdf.format(today.time)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            if (obj.getString("Date") == todayStr) {
                return CalendarDayData(
                    Date = obj.getString("Date"),
                    Gujarati_Month = obj.getString("Gujarati_Month"),
                    Tithi = obj.getString("Tithi"),
                    Day = obj.getString("Day"),
                    Festival_English = obj.optString("Festival_English", "")
                )
            }
        }

        // જો આજની તારીખ JSON માં ન મળે
        return CalendarDayData(
            Date = todayStr,
            Gujarati_Month = "",
            Tithi = "",
            Day = "",
            Festival_English = ""
        )
    }
}
