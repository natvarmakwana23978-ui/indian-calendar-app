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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        val recyclerView = findViewById<RecyclerView>(R.id.calendarListRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // લોડિંગ શરૂ
        progressBar.visibility = View.VISIBLE

        // Retrofit દ્વારા ડેટા મંગાવવો
        RetrofitClient.api.getCalendars().enqueue(object : Callback<List<CalendarItem>> {
            override fun onResponse(call: Call<List<CalendarItem>>, response: Response<List<CalendarItem>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val calendars = response.body() ?: emptyList()
                    
                    // એડપ્ટર સેટ કરવું
                    recyclerView.adapter = CalendarSelectionAdapter(calendars) { selectedItem ->
                        // જ્યારે કોઈ ભાષા પર ક્લિક થાય ત્યારે
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("COL_INDEX", selectedItem.colIndex)
                        intent.putExtra("CALENDAR_NAME", selectedItem.name)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "ડેટા લોડ કરવામાં નિષ્ફળ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarItem>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
