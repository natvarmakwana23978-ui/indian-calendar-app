package com.indian.calendar

import android.os.Bundle
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var calendarGrid: GridView
    private lateinit var monthTitle: TextView
    private lateinit var dayLabels: Array<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // ૧. UI એલિમેન્ટ્સ કનેક્ટ કરવા
        monthTitle = findViewById(R.id.monthTitle)
        calendarGrid = findViewById(R.id.calendarGrid)
        
        // વારના નામ માટેના TextViews (ખાતરી કરો કે XML માં આ ID છે)
        dayLabels = arrayOf(
            findViewById(R.id.sun), findViewById(R.id.mon),
            findViewById(R.id.tue), findViewById(R.id.wed),
            findViewById(R.id.thu), findViewById(R.id.fri),
            findViewById(R.id.sat)
        )

        // ૨. Intent માંથી ડેટા મેળવો
        val jsonData = intent.getStringExtra("DATA") ?: ""
        val selectedCalendar = intent.getStringExtra("SELECTED_CAL") ?: "વિક્રમ સંવત"
        val selectedLanguage = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી"

        monthTitle.text = "$selectedCalendar - $selectedLanguage"

        // ૩. વારના નામ સેટ કરવા (Sheet2 નો પાઇપ વાળો ડેટા)
        setupDayLabels(jsonData, selectedLanguage)

        // ૪. કેલેન્ડરના તારીખના ખાના ભરવા (હાલ પૂરતું સેમ્પલ ડેટા)
        setupCalendarGrid()
    }

    private fun setupDayLabels(jsonData: String, selectedLang: String) {
        try {
            val jsonArray = JSONArray(jsonData)
            var langFound = false

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val langInSheet = obj.optString("language") // ગૂગલ સ્ક્રિપ્ટ મુજબ

                // જો પસંદ કરેલી ભાષા શીટના ડેટા સાથે મેચ થાય
                if (langInSheet.equals(selectedLang, ignoreCase = true)) {
                    val daysArray = obj.getJSONArray("days")
                    for (j in 0 until 7) {
                        dayLabels[j].text = daysArray.getString(j)
                    }
                    langFound = true
                    break
                }
            }

            if (!langFound) {
                // જો ભાષા ન મળે તો ડિફોલ્ટ ગુજરાતી વાર સેટ કરવા
                val defaultDays = arrayOf("રવિ", "સોમ", "મંગળ", "બુધ", "ગુરૂ", "શુક્ર", "શનિ")
                for (i in 0 until 7) dayLabels[i].text = defaultDays[i]
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "વારના નામ લોડ કરવામાં ભૂલ થઈ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCalendarGrid() {
        // અહીં આપણે પછીથી તારીખ અને તિથિઓ ગોઠવવાનું લોજિક ઉમેરીશું
        // અત્યારે આ ખાલી રાખ્યું છે જેથી એપ ક્રેશ ન થાય
    }
}
