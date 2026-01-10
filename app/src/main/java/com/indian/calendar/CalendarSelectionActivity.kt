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
    
    // તમારી નવી અને સાચી URL જે 'ગુજરાતી', 'હિન્દી' ડેટા આપે છે
    private val webAppUrl = "https://script.google.com/macros/s/AKfycbw4BxpTd8aZEMmqVkgtVXdpco8mxBu1E9ikjKkdLdRHjBpn4QPRhMM-HCg0WsVPdGqimA/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarListRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        val btnCreate = findViewById<Button>(R.id.btnCreateNewCalendar)

        // RecyclerView સેટઅપ
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // કેલેન્ડર લિસ્ટ ફેચ કરો
        fetchCalendars()

        // નવું કેલેન્ડર બનાવવા અથવા એડિટ કરવા માટે (પગલું-૩)
        btnCreate.setOnClickListener {
            val intent = Intent(this, ManageCalendarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchCalendars() {
        progressBar.visibility = View.VISIBLE
        
        val request = JsonArrayRequest(Request.Method.GET, webAppUrl, null,
            { response ->
                progressBar.visibility = View.GONE
                calendarList.clear()
                
                try {
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        // 'calendarName' કી તમારી સ્ક્રિપ્ટ મુજબ છે
                        calendarList.add(CalendarModel(
                            item.getString("calendarName"),
                            "Official Community Calendar"
                        ))
                    }
                    
                    // એડેપ્ટર સેટ કરો
                    recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                        // કેલેન્ડર પસંદ થાય ત્યારે ભાષા પસંદગી પર જાઓ (પગલું-૫)
                        val intent = Intent(this, LanguageSelectionActivity::class.java)
                        intent.putExtra("selected_calendar", selected.name)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "ડેટા પ્રોસેસ કરવામાં ભૂલ: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "સર્વર કનેક્શનમાં ભૂલ છે", Toast.LENGTH_SHORT).show()
            }
        )
        
        Volley.newRequestQueue(this).add(request)
    }
}
