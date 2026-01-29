package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.*

class CalendarViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val tvMainHeader = findViewById<TextView>(R.id.tvMainHeader)

        val jsonData = intent.getStringExtra("DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"
        tvMainHeader.text = selectedLang

        val layoutManager = GridLayoutManager(this, 7)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (recyclerView.adapter?.getItemViewType(position) == 0) 7 else 1
            }
        }
        recyclerView.layoutManager = layoutManager

        if (!jsonData.isNullOrEmpty()) {
            val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
            val finalItems = mutableListOf<Any>()

            var currentMonth = ""

            dataList.forEach { json ->
                val dateStr = json.get("ENGLISH").asString // "01/01/2026"
                val parts = dateStr.split("/")
                val month = parts[1]
                val year = parts[2]

                if (month != currentMonth) {
                    currentMonth = month
                    
                    // મહિનાનું નામ
                    val monthName = when(month) {
                        "01" -> "જાન્યુઆરી" "02" -> "ફેબ્રુઆરી" "03" -> "માર્ચ" 
                        "04" -> "એપ્રિલ" "05" -> "મે" "06" -> "જૂન"
                        "07" -> "જુલાઈ" "08" -> "ઓગસ્ટ" "09" -> "સપ્ટેમ્બર"
                        "10" -> "ઓક્ટોબર" "11" -> "નવેમ્બર" "12" -> "ડિસેમ્બર"
                        else -> month
                    }
                    finalItems.add("$monthName $year")

                    // સાચો વાર શોધવા માટેનું લોજિક (૧ તારીખે કયો વાર છે?)
                    val cal = Calendar.getInstance()
                    cal.set(year.toInt(), month.toInt() - 1, 1)
                    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // ૧ = રવિવાર, ૫ = ગુરુવાર

                    // રવિવાર સુધી પહોંચવા માટે જરૂરી ખાલી ખાના ઉમેરો
                    for (i in 1 until firstDayOfWeek) {
                        finalItems.add("EMPTY_SLOT")
                    }
                }
                finalItems.add(CalendarDayData(dateStr, json))
            }
            recyclerView.adapter = CalendarAdapter(finalItems, selectedLang)
        }
    }
}
