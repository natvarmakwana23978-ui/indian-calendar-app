package com.indian.calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LanguageSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        val calendarName = intent.getStringExtra("calendar_name") ?: "Default"

        findViewById<Button>(R.id.btnGujarati).setOnClickListener { setLanguage("gu", calendarName) }
        findViewById<Button>(R.id.btnHindi).setOnClickListener { setLanguage("hi", calendarName) }
        findViewById<Button>(R.id.btnEnglish).setOnClickListener { setLanguage("en", calendarName) }
    }

    private fun setLanguage(langCode: String, calendarName: String) {
        // ભાષાને કાયમી સેવ કરવી
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        sharedPref.edit().putString("selected_lang", langCode).apply()

        // કેલેન્ડર વ્યુ પર જવું
        val intent = Intent(this, CalendarViewActivity::class.java)
        intent.putExtra("calendar_name", calendarName)
        startActivity(intent)
        finish()
    }
}
