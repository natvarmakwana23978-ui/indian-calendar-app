package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val calendarList = mutableListOf<CalendarModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarListRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchCalendars()
    }

    private fun fetchData() {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.getCalendars().enqueue(object : Callback<List<CalendarModel>> {
            override fun onResponse(call: Call<List<CalendarModel>>, response: Response<List<CalendarModel>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    calendarList.clear()
                    calendarList.addAll(list)
                    
                    recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selectedModel, position ->
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        // લિસ્ટમાં જે પોઝિશન પર ક્લિક થાય તે જ શીટનો કોલમ ઇન્ડેક્સ (બાય ડિફોલ્ટ 0 થી શરૂ)
                        intent.putExtra("COLUMN_INDEX", position) 
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<List<CalendarModel>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CalendarSelectionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCalendars() {
        fetchData()
    }
}
