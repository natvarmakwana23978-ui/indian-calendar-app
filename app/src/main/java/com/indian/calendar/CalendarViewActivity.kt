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

        val titleTxt = findViewById<TextView>(R.id.calendarTitleText)
        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        val calendarName = intent.getStringExtra("CALENDAR_NAME") ?: "કેલેન્ડર"
        
        titleTxt?.text = "-:: $calendarName ::-"
        recyclerView?.layoutManager = GridLayoutManager(this, 7)
        progressBar?.visibility = View.VISIBLE

        RetrofitClient.api.getCalendarData(colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar?.visibility = View.GONE
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    
                    // એડપ્ટરમાં ડેટા અને ક્લિક ફંક્શન બંને પાસ કરીએ
                    recyclerView?.adapter = CalendarDayAdapter(data) { selectedDay ->
                        // જ્યારે કોઈ તારીખ પર ક્લિક થાય ત્યારે શું કરવું?
                        // ઉદાહરણ તરીકે: વિગતવાર ફ્રેગમેન્ટ ખોલવો
                        val intent = Intent(this@CalendarViewActivity, CalendarDetailActivity::class.java)
                        intent.putExtra("day_data", selectedDay)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                progressBar?.visibility = View.GONE
                Toast.makeText(this@CalendarViewActivity, "ભૂલ: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
