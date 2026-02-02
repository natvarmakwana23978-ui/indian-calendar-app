package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// અહીં ક્લાસનું નામ તમારી ફાઈલ મુજબ 'CalendarSelectionActivity' રાખ્યું છે
class CalendarSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ખાતરી કરજો કે તમારી XML ફાઈલનું નામ 'activity_calendar_selection' છે
        setContentView(R.layout.activity_calendar_selection)

        val spinnerCalendar = findViewById<Spinner>(R.id.calendarSpinner)
        val spinnerLanguage = findViewById<Spinner>(R.id.languageSpinner)
        val btnNext = findViewById<Button>(R.id.btnOpenCalendar)

        // ૧. લિસ્ટ સેટઅપ
        val calendars = arrayOf("વિક્રમ સંવત (ગુજરાતી)", "Hijri (Islamic)", "Chinese Calendar", "Saka Samvat", "Gregorian (English)", "+ નવું બનાવો")
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)
        spinnerCalendar.adapter = calAdapter

        // ૨. ભાષા પસંદગીનું લોજિક
        spinnerCalendar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCal = calendars[position]
                val languages = when (selectedCal) {
                    "વિક્રમ સંવત (ગુજરાતી)" -> arrayOf("ગુજરાતી", "हिन्दी", "English")
                    "Hijri (Islamic)" -> arrayOf("العربية", "اردો", "ગુજરાતી", "हिन्दी", "English")
                    "Chinese Calendar" -> arrayOf("Mandarin (中文)", "English")
                    else -> arrayOf("English", "ગુજરાતી", "हिन्दी")
                }
                val langAdapter = ArrayAdapter(this@CalendarSelectionActivity, android.R.layout.simple_spinner_dropdown_item, languages)
                spinnerLanguage.adapter = langAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ૩. શીટમાંથી ડેટા ખેંચવો (Sheet2 મુજબ)
        btnNext.setOnClickListener {
            btnNext.isEnabled = false
            btnNext.text = "ડેટા આવી રહ્યો છે..."

            val apiService = RetrofitClient.instance.create(ApiService::class.java)
            
            apiService.getCalendarData("Sheet2", "readAll").enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("DATA", response.body().toString())
                        intent.putExtra("SELECTED_CAL", spinnerCalendar.selectedItem.toString())
                        intent.putExtra("SELECTED_LANG", spinnerLanguage.selectedItem.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@CalendarSelectionActivity, "Sheet2 સાથે કનેક્ટ થવામાં ભૂલ છે!", Toast.LENGTH_LONG).show()
                    }
                    btnNext.isEnabled = true
                    btnNext.text = "આગળ વધો"
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક ચેક કરો: ${t.message}", Toast.LENGTH_LONG).show()
                    btnNext.isEnabled = true
                    btnNext.text = "આગળ વધો"
                }
            })
        }
    }
}
