package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var txtGregorianDate: TextView
    private lateinit var txtSelectedCalendarDate: TextView
    private lateinit var txtSpecialDay: TextView
    private var calendarDataArray: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtGregorianDate = findViewById(R.id.txtGregorianDate)
        txtSelectedCalendarDate = findViewById(R.id.txtVikramDate)
        txtSpecialDay = findViewById(R.id.txtSpecialDay)
        val calendarSpinner = findViewById<Spinner>(R.id.calendarSpinner)

        // કેલેન્ડરના ઓપ્શન્સ (આ કી તમારા JSON મુજબ છે)
        val calendars = arrayOf("Vikram_Samvat", "Jewish", "Hijri", "Saka", "Sikh")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendars)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        calendarSpinner.adapter = adapter

        // JSON લોડ કરો
        loadJSON()

        calendarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateUI(calendars[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadJSON() {
        try {
            val inputStream = assets.open("json/calendar_2082.json")
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            calendarDataArray = JSONArray(jsonText)
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun updateUI(selectedKey: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        
        calendarDataArray?.let {
            for (i in 0 until it.length()) {
                val obj = it.getJSONObject(i)
                if (obj.getString("Date") == currentDate) {
                    
                    // ૧. ગ્રેગોરિયન તારીખ (ભાષા મુજબ)
                    val lang = if (selectedKey == "Vikram_Samvat") Locale("gu") else Locale.US
                    val sdfGregorian = SimpleDateFormat("dd MMMM yyyy (EEEE)", lang)
                    txtGregorianDate.text = sdfGregorian.format(Date())

                    // ૨. પસંદ કરેલ કેલેન્ડરનો ડેટા
                    var rawData = obj.getString(selectedKey)
                    
                    // જો ગુજરાતી સિવાયનું કેલેન્ડર હોય તો અંકો બદલો
                    if (selectedKey != "Vikram_Samvat") {
                        rawData = translateDigitsToEnglish(rawData)
                    }
                    
                    txtSelectedCalendarDate.text = rawData

                    // ૩. વિશેષ દિવસ
                    txtSpecialDay.text = obj.getString("Special_Day")
                    break
                }
            }
        }
    }

    // ગુજરાતી અંકોને અંગ્રેજીમાં ફેરવવાનું લોજિક
    private fun translateDigitsToEnglish(input: String): String {
        return input.replace("૧", "1").replace("૨", "2")
            .replace("૩", "3").replace("૪", "4").replace("૫", "5")
            .replace("૬", "6").replace("૭", "7").replace("૮", "8")
            .replace("૯", "9").replace("૦", "0")
    }
}
