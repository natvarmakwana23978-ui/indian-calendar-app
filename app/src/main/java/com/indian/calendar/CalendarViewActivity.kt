package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var tvMonthYear: TextView
    private lateinit var calendarViewPager: ViewPager2
    private var selectedColIndex: Int = 0
    private var allSheetData: Map<String, CalendarDayData> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        tvMonthYear = findViewById(R.id.tvMonthYear)
        calendarViewPager = findViewById(R.id.calendarViewPager)

        // અગાઉની સ્ક્રીન પરથી પસંદ કરેલ કેલેન્ડરનો ઇન્ડેક્સ મેળવો (ડિફોલ્ટ ૧ - ગુજરાતી)
        selectedColIndex = intent.getIntExtra("COLUMN_INDEX", 1)

        fetchDataFromServer()
    }

    private fun fetchDataFromServer() {
        RetrofitClient.instance.getCalendarData(selectedColIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                if (response.isSuccessful) {
                    val dataList = response.body() ?: emptyList()
                    // ડેટાને તારીખ મુજબ Map માં ગોઠવો જેથી શોધવામાં સરળતા રહે
                    allSheetData = dataList.associateBy { it.date }
                    setupViewPager()
                } else {
                    Toast.makeText(this@CalendarViewActivity, "સર્વર રિસ્પોન્સમાં ભૂલ છે", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                Toast.makeText(this@CalendarViewActivity, "નેટવર્ક કનેક્શન તપાસો: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupViewPager() {
        val startCalendar = Calendar.getInstance()
        // MonthPagerAdapter માં હવે શીટનો ડેટા પાસ થશે
        val adapter = MonthPagerAdapter(this, startCalendar, allSheetData)
        calendarViewPager.adapter = adapter
        calendarViewPager.setCurrentItem(500, false)

        calendarViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, position - 500)
                val sdf = SimpleDateFormat("MMMM yyyy", Locale("gu"))
                tvMonthYear.text = sdf.format(calendar.time)
            }
        })
    }
}
