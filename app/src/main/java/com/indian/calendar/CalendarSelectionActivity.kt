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
    
    // આ તમારી છેલ્લી અને સાચી લિંક છે જે મેં ચેક કરી છે
    private val webAppUrl = "https://script.google.com/macros/s/AKfycbw4BxpTd8aZEMmqVkgtVXdpco8mxBu1E9ikjKkdLdRHjBpn4QPRhMM-HCg0WsVPdGqimA/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarListRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        val btnCreate = findViewById<Button>(R.id.btnCreateNewCalendar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // ડેટા લોડ કરવાનું શરૂ કરો
        fetchCalendarsFromServer()

        btnCreate.setOnClickListener {
            startActivity(Intent(this, ManageCalendarActivity::class.java))
        }
    }

    private fun fetchCalendarsFromServer() {
        progressBar.visibility = View.VISIBLE
        
        val request = JsonArrayRequest(Request.Method.GET, webAppUrl, null,
            { response ->
                progressBar.visibility = View.GONE
                calendarList.clear()
                
                if (response.length() == 0) {
                    Toast.makeText(this, "કોઈ કેલેન્ડર મળ્યા નથી", Toast.LENGTH_SHORT).show()
                }

                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val name = item.getString("calendarName")
                    calendarList.add(CalendarModel(name, "Official Community Calendar"))
                }
                
                recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                    val intent = Intent(this, LanguageSelectionActivity::class.java)
                    intent.putExtra("selected_calendar", selected.name)
                    startActivity(intent)
                }
            },
            { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "સર્વર ભૂલ: લિંક ફરી તપાસો", Toast.LENGTH_LONG).show()
            }
        )
        
        Volley.newRequestQueue(this).add(request)
    }
}
