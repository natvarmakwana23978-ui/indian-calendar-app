package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ૧. કેલેન્ડર બનાવવાનું બટન
        findViewById<Button>(R.id.btnManage).setOnClickListener {
            startActivity(Intent(this, ManageCalendarActivity::class.java))
        }

        // ૨. કેલેન્ડર જોવાનું બટન
        findViewById<Button>(R.id.viewCalendar).setOnClickListener {
            startActivity(Intent(this, CalendarViewActivity::class.java))
        }

        // ૩. રિમાઇન્ડર લિસ્ટ ખોલવાનું બટન
        findViewById<Button>(R.id.btnReminders).setOnClickListener {
            startActivity(Intent(this, RemindersActivity::class.java))
        }
    }
}
