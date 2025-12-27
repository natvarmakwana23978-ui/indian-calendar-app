package com.indian.calendar

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.calendar_spinner) // ખાતરી કરજો કે XML માં આ ID છે

        // ૧. ૨૦ કેલેન્ડરના નામ જે યુઝરને લિસ્ટમાં દેખાશે
        val calendarNames = arrayOf(
            "Vikram Samvat (Gujarati)", "Vikram Samvat (Hindi)", "Shaka Samvat", 
            "Hijri (Islamic)", "Sikh (Nanakshahi)", "Jain Veer Samvat", 
            "Parsi (Shenshai)", "Jewish (Hebrew)", "Chinese Calendar", 
            "Tamil Calendar", "Malayalam Calendar", "Bengali Calendar", 
            "Buddhist Samvat", "Tibetan Calendar", "Ethiopian Calendar", 
            "Coptic Calendar", "Persian Calendar", "Nepal Sambat", 
            "Oriya Calendar", "Assamese Calendar"
        )

        // ૨. આ કી (Keys) આપણા JSON ડેટા સાથે મેચ થાય છે
        val calendarKeys = arrayOf(
            "vikram_gu", "vikram_hi", "shaka_hi", 
            "hijri_ar", "nanakshahi_pa", "jain_veer", 
            "parsi_en", "hebrew_en", "chinese_en", 
            "tamil_ta", "malayalam_ml", "bengali_bn", 
            "buddhist_en", "tibetan_en", "ethiopian_en", 
            "coptic_en", "persian_en", "nepal_sambat", 
            "oriya", "assamese"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendarNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // ૩. જ્યારે યુઝર કેલેન્ડર પસંદ કરે ત્યારે તેને સેવ કરવું
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedKey = calendarKeys[position]
                val sharedPref = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("selected_key", selectedKey)
                    apply()
                }
                // વિજેટને તરત અપડેટ કરવા માટે અહીંથી સૂચના આપી શકાય, પણ હાલ સાદું રાખીએ
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}

