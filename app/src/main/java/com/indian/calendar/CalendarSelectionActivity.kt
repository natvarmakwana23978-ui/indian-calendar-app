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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private fun fetchCalendars() {
        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://script.google.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getCalendars().enqueue(object : Callback<List<CalendarModel>> {
            override fun onResponse(call: Call<List<CalendarModel>>, response: Response<List<CalendarModel>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val list = response.body()
                    if (!list.isNullOrEmpty()) {
                        calendarList.clear()
                        calendarList.addAll(list)
                        recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                            val intent = Intent(this@CalendarSelectionActivity, LanguageSelectionActivity::class.java)
                            intent.putExtra("calendar_name", selected.name)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@CalendarSelectionActivity, "લિસ્ટ ખાલી છે", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "સર્વર ભૂલ: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarModel>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
