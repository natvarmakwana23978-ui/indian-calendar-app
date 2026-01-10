package com.indian.calendar

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ManageCalendarActivity : AppCompatActivity() {

    private val webAppUrl = "https://script.google.com/macros/s/AKfycbygiTARztkRUMQTP9Q45sMSV-PzuVcjoFzk3h2eFEYU5rKQ-s_oQZ13xRKn6udOylVpJQ/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_calendar)

        val btnGenerate = findViewById<Button>(R.id.btnGenerateCalendar)
        val editCalName = findViewById<EditText>(R.id.editCalendarName)
        val editCreator = findViewById<EditText>(R.id.editCreatorName)
        val editMonths = findViewById<EditText>(R.id.editMonthNames)
        val editDays = findViewById<EditText>(R.id.editDayNames)
        val datePicker = findViewById<DatePicker>(R.id.startDatePicker)

        btnGenerate.setOnClickListener {
            val calName = editCalName.text.toString().trim()
            val creator = editCreator.text.toString().trim()
            val months = editMonths.text.toString().trim()
            val days = editDays.text.toString().trim()
            val startDate = "${datePicker.dayOfMonth}-${datePicker.month + 1}-${datePicker.year}"

            if (calName.isEmpty() || creator.isEmpty() || months.isEmpty() || days.isEmpty()) {
                Toast.makeText(this, "બધી વિગતો ભરો", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monthsList = months.split(",").map { it.trim() }
            val daysList = days.split(",").map { it.trim() }

            sendToSheet(calName, creator, monthsList, daysList, startDate)
        }
    }

    private fun sendToSheet(name: String, creator: String, months: List<String>, days: List<String>, date: String) {
        val queue = Volley.newRequestQueue(this)
        val jsonBody = JSONObject()
        jsonBody.put("calendarName", name)
        jsonBody.put("creatorName", creator)
        jsonBody.put("months", JSONArray(months))
        jsonBody.put("days", JSONArray(days))
        jsonBody.put("startDate", date)

        val request = JsonObjectRequest(Request.Method.POST, webAppUrl, jsonBody,
            { Toast.makeText(this, "સફળતાપૂર્વક શેર થયું!", Toast.LENGTH_LONG).show() },
            { Log.e("Error", it.toString()); Toast.makeText(this, "પ્રોસેસ પૂર્ણ થઈ.", Toast.LENGTH_SHORT).show() }
        )
        queue.add(request)
    }
}
