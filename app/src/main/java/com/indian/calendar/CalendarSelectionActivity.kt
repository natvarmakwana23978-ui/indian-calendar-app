package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.databinding.ActivityCalendarSelectionBinding

class CalendarSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarSelectionBinding

    // તમારી પાસે રહેલા ૨૭ કેલેન્ડરના નામની યાદી (આ નામો તમારી ગૂગલ શીટના નામ સાથે મેચ થવા જોઈએ)
    private val calendars = arrayOf(
        "Vikram Samvat 2082", 
        "Shaka Samvat 1947", 
        "Hijri 1447", 
        "Banswara Calendar", 
        "Marathi Calendar"
        // બાકીના નામ અહીં ઉમેરી દેવા...
    )

    private val languages = arrayOf("Gujarati", "Hindi", "English", "Marathi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ૧. View Binding થી લેઆઉટ સેટ કરો
        binding = ActivityCalendarSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ૨. કેલેન્ડર સ્પિનર (Dropdown) સેટઅપ
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)
        binding.calendarSpinner.adapter = calAdapter

        // ૩. ભાષા સ્પિનર સેટઅપ
        val langAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        binding.languageSpinner.adapter = langAdapter

        // ૪. બટન ક્લિક ઇવેન્ટ
        binding.btnOpenCalendar.setOnClickListener {
            val selectedCal = binding.calendarSpinner.selectedItem.toString()
            val selectedLang = binding.languageSpinner.selectedItem.toString()

            // ડેટાને બીજી એક્ટિવિટીમાં મોકલવા માટે Intent
            val intent = Intent(this, CalendarViewActivity::class.java)
            intent.putExtra("SELECTED_CALENDAR", selectedCal)
            intent.putExtra("SELECTED_LANG", selectedLang)
            startActivity(intent)
        }
    }
}
