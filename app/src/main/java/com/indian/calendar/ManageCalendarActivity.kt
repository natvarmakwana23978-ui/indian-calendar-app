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

        val etCalendarName = findViewById<EditText>(R.id.etCalendarName)
        val btnSave = findViewById<Button>(R.id.btnSaveCalendar)

        btnSave.setOnClickListener { view ->
            val name = etCalendarName.text.toString()
            if (name.isNotEmpty()) {
                // અહીં કેલેન્ડર સેવ કરવાનું લોજિક આવશે
                saveCalendarToDatabase(name)
            } else {
                Toast.makeText(this, "મહેરબાની કરીને નામ લખો", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCalendarToDatabase(name: String) {
        // હાલ પૂરતું સફળતાનો મેસેજ બતાવીએ છીએ
        Toast.makeText(this, "$name કેલેન્ડર સેવ થયું!", Toast.LENGTH_SHORT).show()
        finish() // સેવ થયા પછી પાછા જવા માટે
    }
}
