package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

data class CalendarModel(val name: String, val creator: String)

class CalendarSelectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val calendarList = mutableListOf<CalendarModel>()
    private val webAppUrl = "https://script.google.com/macros/s/AKfycbw4BxpTd8aZEMmqVkgtVXdpco8mxBu1E9ikjKkdLdRHjBpn4QPRhMM-HCg0WsVPdGqimA/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarListRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        val btnCreate = findViewById<Button>(R.id.btnCreateNewCalendar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchCalendars()

        btnCreate.setOnClickListener {
            startActivity(Intent(this, ManageCalendarActivity::class.java))
        }
    }

    private fun fetchCalendars() {
        progressBar.visibility = View.VISIBLE
        val request = JsonArrayRequest(Request.Method.GET, webAppUrl, null,
            { response ->
                progressBar.visibility = View.GONE
                calendarList.clear()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    calendarList.add(CalendarModel(item.getString("calendarName"), "Official"))
                }
                recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                    val intent = Intent(this, LanguageSelectionActivity::class.java)
                    intent.putExtra("selected_calendar", selected.name)
                    startActivity(intent)
                }
            },
            {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "ઇન્ટરનેટ ચેક કરો અથવા સર્વર એરર", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }
}
