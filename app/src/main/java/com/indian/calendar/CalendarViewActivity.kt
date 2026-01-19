package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // UI એલિમેન્ટ્સને કનેક્ટ કરીએ
        val titleTxt = findViewById<TextView>(R.id.calendarTitleText)
        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Intent દ્વારા મળેલી માહિતી (ભાષાનો ઇન્ડેક્સ અને નામ)
        val colIndex = intent.getIntExtra("COL_INDEX", 1) // Default 1 (Gujarati)
        val calendarName = intent.getStringExtra("CALENDAR_NAME") ?: "કેલેન્ડર"
        
        // હેડર સેટ કરો
        titleTxt?.text = "-:: $calendarName ::-"
        
        // કેલેન્ડરની ગ્રીડ (૭ વાર મુજબ)
        recyclerView?.layoutManager = GridLayoutManager(this, 7)
        progressBar?.visibility = View.VISIBLE

        // Retrofit દ્વારા ગૂગલ શીટમાંથી ડેટા ખેંચવો [cite: 2026-01-07, 2026-01-14]
        RetrofitClient.api.getCalendarData(colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar?.visibility = View.GONE
                
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    
                    if (data.isNotEmpty()) {
                        // એડપ્ટર સેટ કરીએ અને ક્લિક ઇવેન્ટ હેન્ડલ કરીએ [cite: 2026-01-07]
                        recyclerView?.adapter = CalendarDayAdapter(data, colIndex) { selectedDay ->
                            // જ્યારે કોઈ તારીખ પર ક્લિક થાય ત્યારે વિગતવાર સ્ક્રીન ખોલવી
                            val intent = Intent(this@CalendarViewActivity, CalendarDetailActivity::class.java)
                            intent.putExtra("day_data", selectedDay)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@CalendarViewActivity, "ડેટા મળ્યો નથી!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CalendarViewActivity, "સર્વર ભૂલ: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                progressBar?.visibility = View.GONE
                Toast.makeText(this@CalendarViewActivity, "નેટવર્ક ભૂલ: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
