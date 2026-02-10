package com.smart.reminder

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
// સાચો બાઈન્ડિંગ પાથ અહીં છે
import com.smart.reminder.databinding.ActivityCalendarSelectionBinding

class CalendarSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarSelectionBinding

    private val calendars = arrayOf(
        "Vikram Samvat 2082", 
        "Shaka Samvat 1947", 
        "Hijri 1447", 
        "Banswara Calendar", 
        "Marathi Calendar"
        // બાકીના નામ અહીં ઉમેરી શકાય
    )

    private val languages = arrayOf("Gujarati", "Hindi", "English", "Marathi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // View Binding સેટઅપ
        binding = ActivityCalendarSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // કેલેન્ડર સ્પિનર સેટઅપ
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)
        binding.calendarSpinner.adapter = calAdapter

        // ભાષા સ્પિનર સેટઅપ
        val langAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        binding.languageSpinner.adapter = langAdapter

        // બટન ક્લિક ઇવેન્ટ
        binding.btnOpenCalendar.setOnClickListener {
            val selectedCal = binding.calendarSpinner.selectedItem.toString()
            val selectedLang = binding.languageSpinner.selectedItem.toString()

            val intent = Intent(this, CalendarViewActivity::class.java)
            // ડેટા ટાઇપ સ્પષ્ટ કરવા માટે 'as String' ઉમેર્યું છે
            intent.putExtra("SELECTED_CALENDAR", selectedCal as String)
            intent.putExtra("SELECTED_LANG", selectedLang as String)
            startActivity(intent)
        }
    }
}
