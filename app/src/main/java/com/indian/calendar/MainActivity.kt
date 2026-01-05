package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var txtDate: TextView
    private lateinit var txtPanchang: TextView
    private lateinit var txtFestival: TextView
    private lateinit var txtEmoji: TextView
    private lateinit var calendarSpinner: Spinner
    private lateinit var languageSpinner: Spinner
    private var currentTranslator: Translator? = null

    // કેલેન્ડર લિસ્ટ અને શીટની કોલમ
    private val calendarOptions = listOf(
        "ગુજરાતી (Gujarati)" to 2, "હિન્દી (Hindi)" to 3, "ઇસ્લામિક (Islamic)" to 4,
        "તેલુગુ/કન્નડ" to 5, "તમિલ" to 6, "મલયાલમ" to 7, "પંજાબી" to 8, "થાઈ" to 19, "ફ્રેન્ચ" to 20
    )

    // ભાષા લિસ્ટ અને ML Kit કોડ
    private val languageOptions = listOf(
        "Gujarati" to TranslateLanguage.GUJARATI, "Hindi" to TranslateLanguage.HINDI,
        "Marathi" to TranslateLanguage.MARATHI, "English" to TranslateLanguage.ENGLISH,
        "Spanish" to TranslateLanguage.SPANISH, "French" to TranslateLanguage.FRENCH
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        calendarSpinner = findViewById(R.id.calendarSpinner)
        languageSpinner = findViewById(R.id.languageSpinner)

        setupSpinners()
    }

    private fun setupSpinners() {
        calendarSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendarOptions.map { it.first })
        languageSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageOptions.map { it.first })

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                updateUI()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        calendarSpinner.onItemSelectedListener = listener
        languageSpinner.onItemSelectedListener = listener
    }

    private fun updateUI() {
        val calendarCol = calendarOptions[calendarSpinner.selectedItemPosition].second
        val targetLang = languageOptions[languageSpinner.selectedItemPosition].second
        prepareTranslatorAndFetch(calendarCol, targetLang)
    }

    private fun prepareTranslatorAndFetch(col: Int, lang: String) {
        currentTranslator?.close()
        val options = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH).setTargetLanguage(lang).build()
        currentTranslator = Translation.getClient(options)
        currentTranslator?.downloadModelIfNeeded()?.addOnSuccessListener { fetchSheetData(col) }
    }

    private fun fetchSheetData(colIndex: Int) {
        val today = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        OkHttpClient().newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val lines = response.body?.string()?.split("\n") ?: return
                for (line in lines) {
                    val row = line.split(",")
                    if (row.isNotEmpty() && row[0].trim() == today) {
                        val rawData = row.getOrNull(colIndex) ?: ""
                        currentTranslator?.translate(rawData)?.addOnSuccessListener { translated ->
                            runOnUiThread {
                                txtDate.text = "તારીખ: ${row[0]}/2026"
                                txtPanchang.text = translated
                                txtFestival.text = row.getOrNull(30) ?: ""
                                txtEmoji.text = row.getOrNull(31) ?: ""
                            }
                        }
                        break
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {}
        })
    }
}

