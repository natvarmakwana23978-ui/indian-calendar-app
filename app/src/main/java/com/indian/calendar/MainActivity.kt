package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtTithi = findViewById<TextView>(R.id.txtTithi) // ખાતરી કરો કે XML માં આ ID છે
        val spinner = findViewById<Spinner>(R.id.spinnerOptions)
        val btnSearch = findViewById<Button>(R.id.btnSearchLanguage)

        // આજની સાચી વિગત સેટ કરવી (Manual for testing)
        // 7 Jan 2026 = Posh Vad Pancham
        txtTithi.text = "આજે પોષ વદ પાંચમ છે"

        // સ્પિનર સેટઅપ
        val options = arrayOf("કેલેન્ડર પસંદ કરો", "ગુજરાતી", "હિન્દી", "પર્સનલ નોંધ ઉમેરો")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = options[position]
                if (selected == "પર્સનલ નોંધ ઉમેરો") {
                    try {
                        val intent = Intent(this@MainActivity, ManageCalendarActivity::class.java)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "એક્ટિવિટી મળી નથી!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ભાષા શોધો બટન પર ક્લિક
        btnSearch.setOnClickListener {
            Toast.makeText(this, "ભાષા શોધવાનું ફીચર ટૂંક સમયમાં આવશે", Toast.LENGTH_SHORT).show()
        }
    }
}
