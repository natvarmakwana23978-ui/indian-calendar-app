package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class CalendarViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val tvMainHeader = findViewById<TextView>(R.id.tvMainHeader)

        val jsonData = intent.getStringExtra("DATA")
        val jsonWeekdays = intent.getStringExtra("WEEKDAYS_DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"
        tvMainHeader.text = selectedLang

        val layoutManager = GridLayoutManager(this, 7)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = recyclerView.adapter as? CalendarAdapter
                return if (adapter?.getItemViewType(position) == 0) 7 else 1
            }
        }
        recyclerView.layoutManager = layoutManager

        if (!jsonData.isNullOrEmpty()) {
            val gson = Gson()
            val dataList: List<JsonObject> = gson.fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
            val weekdayList: List<JsonObject> = gson.fromJson(jsonWeekdays, object : TypeToken<List<JsonObject>>() {}.type)

            val localWeekdays = if (weekdayList.isNotEmpty()) {
                val row = weekdayList[0]
                listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday").map { 
                    row.get(it)?.asString ?: it 
                }
            } else listOf("S", "M", "T", "W", "T", "F", "S")

            val finalItems = mutableListOf<Any>()
            var currentMonth = ""

            dataList.forEach { json ->
                val dateStr = json.get("ENGLISH")?.asString ?: ""
                val parts = dateStr.split("/")
                if (parts.size < 3) return@forEach
                
                val month = parts[1]
                if (month != currentMonth) {
                    currentMonth = month
                    finalItems.add("${getMonthName(month)} ${parts[2]}")
                    localWeekdays.forEach { finalItems.add("Header_Day_$it") }

                    val dayName = json.get("Day")?.asString ?: ""
                    val emptyCount = when(dayName.trim()) {
                        "Sun" -> 0 "Mon" -> 1 "Tue" -> 2 "Wed" -> 3 "Thu" -> 4 "Fri" -> 5 "Sat" -> 6 else -> 0
                    }
                    for (i in 0 until emptyCount) finalItems.add("EMPTY_SLOT")
                }
                finalItems.add(CalendarDayData(dateStr, json))
            }
            recyclerView.adapter = CalendarAdapter(finalItems, selectedLang)
        }
    }

    private fun getMonthName(m: String) = when(m) {
        "01" -> "જાન્યુઆરી" "02" -> "ફેબ્રુઆરી" "03" -> "માર્ચ" "04" -> "એપ્રિલ"
        "05" -> "મે" "06" -> "જૂન" "07" -> "જુલાઈ" "08" -> "ઓગસ્ટ"
        "09" -> "સપ્ટેમ્બર" "10" -> "ઓક્ટોબર" "11" -> "નવેમ્બર" "12" -> "ડિસેમ્બર"
        else -> m
    }
}
