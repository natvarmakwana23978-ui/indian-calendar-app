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

class CalendarSelectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val calendarList = mutableListOf<CalendarModel>()
    private val webAppUrl = "https://script.google.com/macros/s/AKfycbxUnuWyrvdmKC13FM6ySQOjMxCFIdH-hHkUen--kRmuYDh1BI09BCrcLV4J-5Wd3uI/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarListRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        val btnCreate = findViewById<Button>(R.id.btnCreateNewCalendar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchCalendars()

        btnCreate.setOnClickListener {
            // STEP 3: જો યુઝર નવું બનાવવા માંગે તો
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
                    // STEP 5: કેલેન્ડર પસંદ કર્યા પછી ભાષા પસંદગી પર જાઓ
                    startActivity(Intent(this, LanguageSelectionActivity::class.java))
                }
            },
            {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "લિસ્ટ લોડ કરવામાં ભૂલ છે", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }
}
