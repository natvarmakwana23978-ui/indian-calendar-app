package com.indian.calendar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.indian.calendar.databinding.ActivityCalendarViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedCal = intent.getStringExtra("SELECTED_CALENDAR") ?: "Vikram Samvat 2082"
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "Gujarati"

        binding.recyclerViewCalendar.layoutManager = GridLayoutManager(this, 7)

        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.getCalendarData(selectedCal, "read").enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    binding.recyclerViewCalendar.adapter = CalendarAdapter(data, selectedLang)
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                Toast.makeText(this@CalendarViewActivity, "એરર: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
