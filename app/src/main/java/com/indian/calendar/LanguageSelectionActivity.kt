package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.indian.calendar.databinding.ActivityLanguageSelectionBinding

class LanguageSelectionActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLanguageSelectionBinding
    private lateinit var adapter: LanguageSelectionAdapter
    
    // ગૂગલ શીટમાંથી 35 ભાષાઓ (સેમ્પલ)
    private val languageList = listOf(
        LanguageItem("en", "English"),
        LanguageItem("en_hi", "English (Hindi)"),
        LanguageItem("en_es", "English (Spanish)"),
        LanguageItem("en_fr", "English (French)"),
        LanguageItem("gu", "Gujarati"),
        LanguageItem("hi", "हिन्दी"),
        // ... 29 more
        LanguageItem("vi", "English (Vietnamese)")
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Selected calendar info
        val calendarId = intent.getStringExtra("CALENDAR_ID") ?: "1"
        val calendarName = intent.getStringExtra("CALENDAR_NAME") ?: "Vikram Samvat 2082"
        
        binding.tvSelectedCalendar.text = "Selected: $calendarName"
        
        setupRecyclerView(calendarId)
    }
    
    private fun setupRecyclerView(calendarId: String) {
        adapter = LanguageSelectionAdapter(languageList) { language ->
            // ભાષા સિલેક્ટ કર્યા પછી Calendar View પર જાવ
            val intent = Intent(this, CalendarViewActivity::class.java)
            intent.putExtra("CALENDAR_ID", calendarId)
            intent.putExtra("LANGUAGE_CODE", language.code)
            startActivity(intent)
        }
        
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }
}

data class LanguageItem(
    val code: String,
    val name: String
)
