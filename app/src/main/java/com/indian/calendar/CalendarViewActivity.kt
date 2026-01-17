package com.indian.calendar

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

        // XML માંથી વ્યુઝ મેળવવા
        val titleTxt = findViewById<TextView>(R.id.calendarTitleText)
        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // અગાઉની સ્ક્રીન (લિસ્ટ) માંથી પસંદ કરેલ કેલેન્ડરનો ડેટા મેળવવો
        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        val calendarName = intent.getStringExtra("CALENDAR_NAME") ?: "કેલેન્ડર"
        
        titleTxt?.text = calendarName

        // કેલેન્ડરને ૭ કોલમ (વાર મુજબ) માં ગોઠવવા માટે
        recyclerView?.layoutManager = GridLayoutManager(this, 7)
        
        progressBar?.visibility = View.VISIBLE

        // ગૂગલ શીટમાંથી ડેટા લોડ કરવો
        RetrofitClient.api.getCalendarData(colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar?.visibility = View.GONE
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    
                    // અહીં આપણે ડેટાને એડપ્ટર સાથે જોડીએ છીએ
                    if (data.isNotEmpty()) {
                        recyclerView?.adapter = CalendarDayAdapter(data)
                    } else {
                        Toast.makeText(this@CalendarViewActivity, "કોઈ ડેટા મળ્યો નથી", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                progressBar?.visibility = View.GONE
                Toast.makeText(this@CalendarViewActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
