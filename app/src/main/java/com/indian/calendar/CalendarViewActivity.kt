package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalendarAdapter
    private val webAppUrl = "તમારી_URL_અહીં_નાખો" // તમારી Apps Script URL પેસ્ટ કરો

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        recyclerView = findViewById(R.id.calendarRecyclerView)
        // ૭ કોલમવાળી ગ્રીડ (અઠવાડિયાના ૭ દિવસ માટે)
        recyclerView.layoutManager = GridLayoutManager(this, 7)

        fetchCalendarData()
    }

    private fun fetchCalendarData() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, webAppUrl,
            { response ->
                val daysList = parseSheetData(response)
                adapter = CalendarAdapter(daysList)
                recyclerView.adapter = adapter
            },
            { error ->
                Toast.makeText(this, "ડેટા લોડ કરવામાં ભૂલ: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)
    }

    private fun parseSheetData(jsonResponse: String): List<String> {
        val list = mutableListOf<String>()
        try {
            val jsonArray = JSONArray(jsonResponse)
            // આપણે ધારીએ છીએ કે શીટની છેલ્લી રો માં લેટેસ્ટ કેલેન્ડર છે
            if (jsonArray.length() > 0) {
                val lastRow = jsonArray.getJSONArray(jsonArray.length() - 1)
                // આપણી શીટમાં ચોથા નંબરે (Index 3) 'days' લિસ્ટ છે
                val daysArray = JSONArray(lastRow.getString(3))
                for (i in 0 until daysArray.length()) {
                    list.add(daysArray.getString(i))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}

