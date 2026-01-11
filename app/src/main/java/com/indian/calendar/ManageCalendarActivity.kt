package com.indian.calendar

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ManageCalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_calendar)

        // findViewById નો ઉપયોગ કરીને ID જોડો
        val etName = findViewById<EditText>(R.id.etCalendarName)
        val btnSave = findViewById<Button>(R.id.btnSaveCalendar)

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            if (name.isNotEmpty()) {
                Toast.makeText(this, "$name કેલેન્ડર સેવ થયું!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "મહેરબાની કરીને નામ લખો", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
