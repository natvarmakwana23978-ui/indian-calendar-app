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

        // જો ID ન મળે તો એપ ક્રેશ ન થાય તે માટે આ સાચું ID વાપરો
        val titleTxt = findViewById<TextView>(R.id.calendarTitleText) ?: findViewById(R.id.calendarTitle)
        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        val calendarName = intent.getStringExtra("CALENDAR_NAME") ?: "Calendar"
        titleTxt?.text = calendarName

        recyclerView.layoutManager = GridLayoutManager(this, 7)
        progressBar.visibility = View.VISIBLE

        RetrofitClient.api.getCalendarData(colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    // Adapter લોજિક અહીં આવશે
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CalendarViewActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
