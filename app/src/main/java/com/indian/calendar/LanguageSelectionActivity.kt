package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.databinding.ActivityCalendarSelectionBinding 

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityCalendarSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // અહીં btnNext ને બદલે btnOpenCalendar વાપરવું
        binding.btnOpenCalendar.setOnClickListener {
            
            val selectedLang = binding.languageSpinner.selectedItem.toString()
            // જો કેલેન્ડર સિલેક્શન પણ લેવું હોય તો:
            val selectedCalendar = binding.calendarSpinner.selectedItem.toString()
            
            val intent = Intent(this, CalendarSelectionActivity::class.java)
            intent.putExtra("SELECTED_LANG", selectedLang)
            intent.putExtra("SELECTED_CALENDAR", selectedCalendar)
            startActivity(intent)
        }
    }
}
