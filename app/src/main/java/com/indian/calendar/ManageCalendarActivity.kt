package com.indian.calendar

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ManageCalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_calendar)

        val btnGenerate = findViewById<Button>(R.id.btnGenerateCalendar)
        val editMonthNames = findViewById<EditText>(R.id.editMonthNames)
        val editDayNames = findViewById<EditText>(R.id.editDayNames)
        val datePicker = findViewById<DatePicker>(R.id.startDatePicker)

        btnGenerate.setOnClickListener {
            // ૧. ઇનપુટ મેળવવો
            val rawMonths = editMonthNames.text.toString().trim()
            val rawDays = editDayNames.text.toString().trim()
            val startDate = "${datePicker.dayOfMonth}-${datePicker.month + 1}-${datePicker.year}"

            // ૨. વેલિડેશન (ખાતરી કરવી કે વિગતો અધૂરી નથી)
            if (rawMonths.isEmpty() || rawDays.isEmpty()) {
                Toast.makeText(this, "મહેરબાની કરીને બધી વિગતો ભરો", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monthsList = rawMonths.split(",").map { it.trim() }
            val daysList = rawDays.split(",").map { it.trim() }

            if (monthsList.size < 12) {
                Toast.makeText(this, "ઓછામાં ઓછા ૧૨ મહિનાના નામ લખો", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ૩. ડેટા મોકલવાનું ફંક્શન કોલ કરવું
            sendToGoogleSheet(monthsList, daysList, startDate)
        }
    }

    private fun sendToGoogleSheet(months: List<String>, days: List<String>, date: String) {
        // આ URL ની જગ્યાએ તમારી પોતાની Script URL નાખવાની રહેશે
        val url = "https://script.google.com/macros/s/AKfycbw7U_En4xgEayUOV3N3HwoW-GkfyOONMUxidIeZbOAxCfGJqxcXeWFvz6pnJL5nRQQ3/exec"
        
        val queue = Volley.newRequestQueue(this)
        val jsonBody = JSONObject()
        jsonBody.put("calendarName", "Community Calendar")
        jsonBody.put("months", JSONArray(months))
        jsonBody.put("days", JSONArray(days))
        jsonBody.put("startDate", date)

        val request = JsonObjectRequest(Request.Method.POST, url, jsonBody,
            { response ->
                Toast.makeText(this, "કેલેન્ડર સફળતાપૂર્વક શેર થયું!", Toast.LENGTH_LONG).show()
            },
            { error ->
                // ગૂગલ સ્ક્રિપ્ટમાં ઘણીવાર સક્સેસ હોવા છતાં રીડાયરેક્ટના લીધે એરર બતાવે છે
                Toast.makeText(this, "પ્રોસેસ પૂર્ણ થઈ. શીટ ચેક કરો.", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }
}
