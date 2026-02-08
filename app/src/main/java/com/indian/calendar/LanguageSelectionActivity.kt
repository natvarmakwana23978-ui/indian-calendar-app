package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
// જો ફાઈલનું નામ activity_calendar_selection.xml હોય, તો બાઈન્ડિંગ આ જ રહેશે:
import com.indian.calendar.databinding.ActivityCalendarSelectionBinding 

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // અહીં બાઈન્ડિંગ સેટ કરીએ છીએ
        binding = ActivityCalendarSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            // ખાતરી કરજો કે તમારા XML માં Spinner નું ID 'languageSpinner' જ હોય
            val selectedLang = binding.languageSpinner.selectedItem.toString()
            
            val intent = Intent(this, CalendarSelectionActivity::class.java)
            intent.putExtra("SELECTED_LANG", selectedLang)
            startActivity(intent)
        }
    }
}
