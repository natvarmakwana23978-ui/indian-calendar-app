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

    // તમારી શીટ મુજબ દરેક ભાષા અને તેનો ચોક્કસ કોલમ ઇન્ડેક્સ
    private val languageData = listOf(
        LangConfig("ગુજરાતી (Gujarati)", 2, "gu"),
        LangConfig("હિન્દી (Hindi)", 3, "hi"),
        LangConfig("ઇસ્લામિક (Islamic)", 4, "ar"),
        LangConfig("તેલુગુ/કન્નડ (Telugu/Kannada)", 5, "te"),
        LangConfig("તમિલ (Tamil)", 6, "ta"),
        LangConfig("મલયાલમ (Malayalam)", 7, "ml"),
        LangConfig("પંજાબી (Punjabi)", 8, "pa"),
        LangConfig("ઓડિયા (Odia)", 9, "or"),
        LangConfig("બંગાળી (Bengali)", 10, "bn"),
        LangConfig("નેપાળી (Nepali)", 11, "ne"),
        LangConfig("ચાઇનીઝ (Chinese)", 12, "zh"),
        LangConfig("હિબ્રુ (Hebrew)", 13, "he"),
        LangConfig("પર્શિયન (Persian)", 14, "fa"),
        LangConfig("ઇથોપિયન (Ethiopian)", 15, "am"),
        LangConfig("બાલીનીઝ (Balinese)", 16, "ban"),
        LangConfig("કોરિયન (Korean)", 17, "ko"),
        LangConfig("વિયેતનામીસ (Vietnamese)", 18, "vi"),
        LangConfig("થાઈ (Thai)", 19, "th"),
        LangConfig("ફ્રેન્ચ (French)", 20, "fr"),
        LangConfig("બર્મીઝ (Burmese)", 21, "my"),
        LangConfig("કાશ્મીરી (Kashmiri)", 22, "ks"),
        LangConfig("મારવાડી (Marwari)", 23, "mwr"),
        LangConfig("જાપાનીઝ (Japanese)", 24, "ja"),
        LangConfig("અસામી (Assamese)", 25, "as"),
        LangConfig("સિંધી (Sindhi)", 26, "sd"),
        LangConfig("તિબેટીયન (Tibetan)", 27, "bo")
    )

    data class LangConfig(val name: String, val columnIndex: Int, val isoCode: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        languageSpinner = findViewById(R.id.languageSpinner)

        setupLanguageMenu()
        autoDetectLanguage()
    }

    private fun setupLanguageMenu() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageData.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                fetchSheetData(languageData[pos])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun autoDetectLanguage() {
        val systemLang = Locale.getDefault().language
        val index = languageData.indexOfFirst { it.isoCode == systemLang }
        if (index != -1) {
            languageSpinner.setSelection(index)
        }
    }

    private fun fetchSheetData(config: LangConfig) {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val today = sdf.format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        OkHttpClient().newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val csv = response.body?.string() ?: ""
                val lines = csv.split("\n")
                for (line in lines) {
                    val row = line.split(",")
                    if (row.isNotEmpty() && row[0].trim() == today) {
                        
                        // અહીં ભાષાંતર કરવાને બદલે સીધો તે ભાષાની કોલમનો ડેટા લો
                        val localData = row.getOrNull(config.columnIndex)?.trim() ?: "No Data"
                        val festival = row.getOrNull(30)?.trim() ?: ""
                        val emoji = row.getOrNull(31)?.trim() ?: ""

                        runOnUiThread {
                            txtDate.text = "તારીખ: ${row[0]}/2026"
                            txtPanchang.text = localData // હવે તે જે-તે દેશનું સાચું કેલેન્ડર બતાવશે
                            txtFestival.text = festival
                            txtEmoji.text = emoji
                        }
                        break
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {}
        })
    }
}

