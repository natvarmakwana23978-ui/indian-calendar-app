package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var txtDate: TextView
    private lateinit var txtPanchang: TextView
    private lateinit var txtFestival: TextView
    private lateinit var txtEmoji: TextView
    private lateinit var languageSpinner: Spinner

    // તમારી શીટ મુજબ ભાષાઓ અને તેમની કોલમનો ઇન્ડેક્સ (તારીખ કોલમ 0 છે)
    private val languageData = listOf(
        LangConfig("ગુજરાતી (Gujarati)", 1),
        LangConfig("હિન્દી (Hindi)", 2),
        LangConfig("ઇસ્લામિક (Islamic)", 3),
        LangConfig("તેલુગુ/કન્નડ (Telugu/Kannada)", 4),
        LangConfig("તમિલ (Tamil)", 5),
        LangConfig("મલયાલમ (Malayalam)", 6),
        LangConfig("પંજાબી (Punjabi)", 7),
        LangConfig("ઓડિયા (Odia)", 8),
        LangConfig("બંગાળી (Bengali)", 9),
        LangConfig("નેપાળી (Nepali)", 10),
        LangConfig("ચાઇનીઝ (Chinese)", 11),
        LangConfig("હિબ્રુ (Hebrew)", 12),
        LangConfig("પર્શિયન (Persian)", 13),
        LangConfig("ઇથોપિયન (Ethiopian)", 14),
        LangConfig("બાલીનીઝ (Balinese)", 15),
        LangConfig("કોરિયન (Korean)", 16),
        LangConfig("વિયેતનામીસ (Vietnamese)", 17),
        LangConfig("થાઈ (Thai)", 18),
        LangConfig("ફ્રેન્ચ (French)", 19),
        LangConfig("બર્મીઝ (Burmese)", 20),
        LangConfig("કાશ્મીરી (Kashmiri)", 21),
        LangConfig("મારવાડી (Marwari)", 22),
        LangConfig("જાપાનીઝ (Japanese)", 23),
        LangConfig("અસામી (Assamese)", 24),
        LangConfig("સિંધી (Sindhi)", 25),
        LangConfig("તિબેટીયન (Tibetan)", 26)
    )

    data class LangConfig(val name: String, val columnIndex: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        languageSpinner = findViewById(R.id.languageSpinner)

        setupLanguageMenu()
    }

    private fun setupLanguageMenu() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageData.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fetchSheetData(languageData[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fetchSheetData(config: LangConfig) {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val today = sdf.format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { txtPanchang.text = "ઇન્ટરનેટ કનેક્શન તપાસો." }
            }

            override fun onResponse(call: Call, response: Response) {
                val csvContent = response.body?.string() ?: ""
                val lines = csvContent.split("\n")

                for (line in lines) {
                    val row = line.split(",")
                    // row[0] માં તારીખ છે
                    if (row.isNotEmpty() && row[0].contains(today)) {
                        
                        // પસંદ કરેલી ભાષાનો ડેટા તેની કોલમમાંથી લો
                        val localCalendarData = if (row.size > config.columnIndex) row[config.columnIndex] else "ડેટા ઉપલબ્ધ નથી"
                        
                        runOnUiThread {
                            txtDate.text = "આજની તારીખ: ${row[0]}/2026"
                            txtPanchang.text = "${config.name}:\n$localCalendarData"
                            
                            // તહેવાર (કોલમ 30) અને ઇમોજી (કોલમ 31)
                            if (row.size > 30) txtFestival.text = row[30]
                            if (row.size > 31) txtEmoji.text = row[31]
                        }
                        break
                    }
                }
            }
        })
    }
}

