package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // "કેલેન્ડર જૂઓ" બટન
        findViewById<Button>(R.id.btnViewCalendar).setOnClickListener {
            val intent = Intent(this, CalendarSelectionActivity::class.java)
            startActivity(intent)
        }

        // બાકીના બે બટન અત્યારે ખાલી રાખીએ જેથી એપ ક્રેશ ન થાય
        findViewById<Button>(R.id.btnCreateCalendar).setOnClickListener { }
        findViewById<Button>(R.id.btnReminders).setOnClickListener { }
    }
}
