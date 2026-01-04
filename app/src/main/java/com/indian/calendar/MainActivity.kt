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

    // તમારી શીટ મુજબ કોલમ સેટઅપ (તારીખ 0, ગુજરાતી 2, હિન્દી 3...)
    private val languageData = listOf(
        LangConfig("ગુજરાતી (Gujarati)", 2),
        LangConfig("હિન્દી (Hindi)", 3),
        LangConfig("ઇસ્લામિક (Islamic)", 4),
        LangConfig("તેલુગુ/કન્નડ (Telugu/Kannada)", 5),
        LangConfig("તમિલ (Tamil)", 6),
        LangConfig("મલયાલમ (Malayalam)", 7),
        LangConfig("પંજાબી (Punjabi)", 8),
        LangConfig("ઓડિયા (Odia)", 9),
        LangConfig("બંગાળી (Bengali)", 10),
        LangConfig("નેપાળી (Nepali)", 11),
        LangConfig("ચાઇનીઝ (Chinese)", 12),
        LangConfig("હિબ્રુ (Hebrew)", 13),
        LangConfig("પર્શિયન (Persian)", 14),
        LangConfig("ઇથોપિયન (Ethiopian)", 15),
        LangConfig("બાલીનીઝ (Balinese)", 16),
        LangConfig("કોરિયન (Korean)", 17),
        LangConfig("વિયેતનામીસ (Vietnamese)", 18),
        LangConfig("થાઈ (Thai)", 19),
        LangConfig("ફ્રેન્ચ (French)", 20),
        LangConfig("બર્મીઝ (Burmese)", 21),
        LangConfig("કાશ્મીરી (Kashmiri)", 22),
        LangConfig("મારવાડી (Marwari)", 23),
        LangConfig("જાપાનીઝ (Japanese)", 24),
        LangConfig("અસામી (Assamese)", 25),
        LangConfig("સિંધી (Sindhi)", 26),
        LangConfig("તિબેટીયન (Tibetan)", 27)
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
        // આજની તારીખ મેળવો (દા.ત. 04/01)
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val todayStr = sdf.format(Date())

        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { txtPanchang.text = "Error connecting to server." }
            }

            override fun onResponse(call: Call, response: Response) {
                val csvContent = response.body?.string() ?: ""
                val lines = csvContent.split("\n")

                for (line in lines) {
                    val row = line.split("\t") // શીટમાં ઘણીવાર ટેબ (Tab) હોય છે, નહીતર "," વાપરો
                    val cleanRow = if (row.size < 5) line.split(",") else row

                    if (cleanRow.isNotEmpty() && cleanRow[0].trim() == todayStr) {
                        
                        // ડેટા મેળવો
                        val calendarInfo = cleanRow.getOrNull(config.columnIndex)?.trim() ?: "N/A"
                        val festivalInfo = cleanRow.getOrNull(30)?.trim() ?: ""
                        val emojiInfo = cleanRow.getOrNull(31)?.trim() ?: ""

                        runOnUiThread {
                            txtDate.text = "તારીખ: ${cleanRow[0]}/2026"
                            txtPanchang.text = "${config.name}:\n$calendarInfo"
                            txtFestival.text = festivalInfo
                            txtEmoji.text = emojiInfo
                        }
                        break
                    }
                }
            }
        })
    }
}

