package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.databinding.ActivityLanguageSelectionBinding

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageSelectionBinding
    private val languages = arrayOf("Gujarati", "Hindi", "English", "Marathi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        binding.languageSpinner.adapter = adapter

        binding.btnNext.setOnClickListener {
            val selectedLang = binding.languageSpinner.selectedItem.toString()
            val intent = Intent(this, CalendarSelectionActivity::class.java)
            intent.putExtra("SELECTED_LANG", selectedLang)
            startActivity(intent)
        }
    }
}
