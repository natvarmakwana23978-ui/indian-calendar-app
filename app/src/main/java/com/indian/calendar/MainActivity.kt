package com.indian.calendar

import android.content.Context
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
    private lateinit var calendarSpinner: Spinner
    private var calendarDataArray: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // XML માંથી વ્યૂઝને લિંક કરો
        txtGregorianDate = findViewById(R.id.txtGregorianDate)
        txtSelectedCalendarDate = findViewById(R.id.txtVikramDate)
        txtSpecialDay = findViewById(R.id.txtSpecialDay)
        calendarSpinner = findViewById(R.id.calendarSpinner)

        // તમારી JSON ફાઈલ મુજબની કી (Keys)
        val calendars = arrayOf("Vikram_Samvat", "Jewish", "Hijri", "Saka", "Parsi", "Sikh")
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendars)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        calendarSpinner.adapter = adapter

        // અગાઉ પસંદ કરેલું કેલેન્ડર યાદ રાખવા માટે
        val sharedPref = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
        val savedPos = sharedPref.getInt("selected_pos", 0)
        calendarSpinner.setSelection(savedPos)

        loadJSON()

        calendarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // પસંદગી સાચવી લો
                sharedPref.edit().putInt("selected_pos", position).apply()
                sharedPref.edit().putString("selected_key", calendars[position]).apply()
                
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
                    
                    // ૧. ગ્રેગોરિયન તારીખ (ભાષા લોજિક)
                    val lang = if (selectedKey == "Vikram_Samvat") Locale("gu") else Locale.US
                    val sdfGregorian = SimpleDateFormat("dd MMMM yyyy (EEEE)", lang)
                    txtGregorianDate.text = sdfGregorian.format(Date())

                    // ૨. પસંદ કરેલ કેલેન્ડરનો ડેટા અને અંકોનું પરિવર્તન
                    var rawData = obj.getString(selectedKey)
                    if (selectedKey != "Vikram_Samvat") {
                        rawData = translateDigitsToEnglish(rawData)
                    }
                    txtSelectedCalendarDate.text = rawData

                    // ૩. વિશેષ દિવસ
                    val special = obj.getString("Special_Day")
                    txtSpecialDay.text = if (special == "--") "તપાસી રહ્યું છે..." else "આજે $special છે."
                    break
                }
            }
        }
    }

    private fun translateDigitsToEnglish(input: String): String {
        return input.replace("૧", "1").replace("૨", "2")
            .replace("૩", "3").replace("૪", "4").replace("૫", "5")
            .replace("૬", "6").replace("૭", "7").replace("૮", "8")
            .replace("૯", "9").replace("૦", "0")
    }
}
