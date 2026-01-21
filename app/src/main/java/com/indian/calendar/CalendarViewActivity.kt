package com.indian.calendar
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)
        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        recyclerView.layoutManager = GridLayoutManager(this, 7)

        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        progressBar.visibility = View.VISIBLE
        RetrofitClient.api.getCalendarData(colIndex = colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    recyclerView.adapter = CalendarAdapter(response.body() ?: emptyList())
                }
            }
            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) { progressBar.visibility = View.GONE }
        })
    }
}
