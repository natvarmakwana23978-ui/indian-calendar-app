package com.indian.calendar

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ManageCalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_calendar)

        val btnGenerate = findViewById<Button>(R.id.btnGenerateCalendar)
        val editMonthNames = findViewById<EditText>(R.id.editMonthNames)
        val editDayNames = findViewById<EditText>(R.id.editDayNames)
        val datePicker = findViewById<DatePicker>(R.id.startDatePicker)

        btnGenerate.setOnClickListener {
            // ૧. ડેટા મેળવવો અને અલ્પવિરામથી અલગ કરવો
            val rawMonths = editMonthNames.text.toString().trim()
            val rawDays = editDayNames.text.toString().trim()

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

            // ૩. પસંદ કરેલી તારીખ મેળવવી
            val startDay = datePicker.dayOfMonth
            val startMonth = datePicker.month + 1 // મહિનો 0 થી શરૂ થાય એટલે +1
            val startYear = datePicker.year

            // ૪. હવે આ ડેટાને આપણે ગૂગલ શીટમાં મોકલવા માટે તૈયાર છીએ
            processCalendarData(monthsList, daysList, startDay, startMonth, startYear)
        }
    }

    private fun processCalendarData(months: List<String>, days: List<String>, d: Int, m: Int, y: Int) {
        // અહીં આપણે ડેટા પ્રિન્ટ કરીને ચેક કરીએ છીએ (લોગમાં દેખાશે)
        println("New Calendar Logic: Starts on $d-$m-$y")
        println("Months: $months")
        println("Days: $days")

        Toast.makeText(this, "માહિતી સફળતાપૂર્વક લેવામાં આવી છે!", Toast.LENGTH_LONG).show()
        
        // હવે પછીના કામમાં આપણે આ ડેટાને Google Sheet API સાથે જોડીશું
    }
}
