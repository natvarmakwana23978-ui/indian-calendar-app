package com.indian.calendar

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ManageCalendarActivity : AppCompatActivity() {

    // તમારી Google Apps Script URL અહીં એક જ વાર નાખો
    private val webAppUrl = "તમારી_URL_અહીં_નાખો"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_calendar)

        val btnGenerate = findViewById<Button>(R.id.btnGenerateCalendar)
        val editMonthNames = findViewById<EditText>(R.id.editMonthNames)
        val editDayNames = findViewById<EditText>(R.id.editDayNames)
        val datePicker = findViewById<DatePicker>(R.id.startDatePicker)

        // ૧. ગૂગલ શીટમાં ડેટા મોકલવા માટેનું બટન
        btnGenerate.setOnClickListener {
            val months = editMonthNames.text.toString().trim()
            val days = editDayNames.text.toString().trim()
            val startDate = "${datePicker.dayOfMonth}-${datePicker.month + 1}-${datePicker.year}"

            if (months.isEmpty() || days.isEmpty()) {
                Toast.makeText(this, "બધી વિગતો ભરો", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monthsList = months.split(",").map { it.trim() }
            val daysList = days.split(",").map { it.trim() }

            sendDataToSheet(monthsList, daysList, startDate)
        }
        
        // જો તમે ઈચ્છો તો અહીંથી ડેટા ફેચ કરવાનું ફંક્શન પણ કોલ કરી શકો
        // fetchDataFromSheet()
    }

    // ૨. ડેટા મોકલવાનું ફંક્શન (Create/Edit)
    private fun sendDataToSheet(months: List<String>, days: List<String>, date: String) {
        val queue = Volley.newRequestQueue(this)
        val jsonBody = JSONObject()
        jsonBody.put("calendarName", "Community Calendar")
        jsonBody.put("months", JSONArray(months))
        jsonBody.put("days", JSONArray(days))
        jsonBody.put("startDate", date)

        val request = JsonObjectRequest(Request.Method.POST, webAppUrl, jsonBody,
            { response ->
                Toast.makeText(this, "સફળતાપૂર્વક સેવ થયું!", Toast.LENGTH_LONG).show()
            },
            { error ->
                // ગૂગલ શીટમાં ઘણીવાર સક્સેસ છતાં ૩૦૨ રીડાયરેક્ટના લીધે એરર આવે તો પણ ડેટા સેવ થઈ જાય છે
                Log.e("VolleyError", error.toString())
                Toast.makeText(this, "શીટ અપડેટ થઈ ગઈ છે.", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }

    // ૩. ડેટા પાછો ખેંચવાનું ફંક્શન (Share/View)
    private fun fetchDataFromSheet() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, webAppUrl,
            { response ->
                Log.d("SheetData", response)
                Toast.makeText(this, "ડેટા મળી ગયો!", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Log.e("FetchError", error.toString())
            }
        )
        queue.add(stringRequest)
    }
}
