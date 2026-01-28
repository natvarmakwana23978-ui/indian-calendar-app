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
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"
        tvMainHeader.text = selectedLang

        val layoutManager = GridLayoutManager(this, 7)
        // હેડરને આખી લાઈનમાં ફેલાવવા માટે SpanSizeLookup
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (recyclerView.adapter?.getItemViewType(position) == 0) 7 else 1
            }
        }
        recyclerView.layoutManager = layoutManager

        if (!jsonData.isNullOrEmpty()) {
            val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
            val finalItems = mutableListOf<Any>()

            // મહિના મુજબ ડેટા ગ્રુપિંગ (સરળ રીત)
            var currentMonth = ""
            var saturdayCount = 0

            dataList.forEachIndexed { index, json ->
                val dateParts = json.get("ENGLISH").asString.split("/")
                val month = dateParts[1] // MM ભાગ
                val year = dateParts[2] // YYYY ભાગ

                if (month != currentMonth) {
                    currentMonth = month
                    saturdayCount = 0 // નવો મહિનો શરૂ થતા શનિવારની ગણતરી ફરીથી ૦ કરો
                    
                    val monthName = when(month) {
                        "01" -> "જાન્યુઆરી" "02" -> "ફેબ્રુઆરી" "03" -> "માર્ચ" "04" -> "એપ્રિલ"
                        "05" -> "મે" "06" -> "જૂન" "07" -> "જુલાઈ" "08" -> "ઓગસ્ટ"
                        "09" -> "સપ્ટેમ્બર" "10" -> "ઓક્ટોબર" "11" -> "નવેમ્બર" "12" -> "ડિસેમ્બર"
                        else -> month
                    }
                    finalItems.add("$monthName $year") // હેડર ઉમેરો
                    
                    // મહિનાની શરૂઆતના ખાલી ખાના (જાન્યુઆરી માટે ૪)
                    if (month == "01") {
                        for (i in 0 until 4) finalItems.add("EMPTY")
                    }
                }
                finalItems.add(CalendarDayData(json.get("ENGLISH").asString, json))
            }
            recyclerView.adapter = CalendarAdapter(finalItems, selectedLang)
        }
    }
}
