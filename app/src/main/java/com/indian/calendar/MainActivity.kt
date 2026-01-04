package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
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

    // એરર ન આવે તે માટે સાચા ગ્લોબલ ભાષા કોડનું લિસ્ટ
    private val languageMap = mapOf(
        "Gujarati" to TranslateLanguage.GUJARATI,
        "Hindi" to TranslateLanguage.HINDI,
        "Marathi" to TranslateLanguage.MARATHI,
        "Bengali" to TranslateLanguage.BENGALI,
        "Tamil" to TranslateLanguage.TAMIL,
        "Telugu" to TranslateLanguage.TELUGU,
        "Kannada" to TranslateLanguage.KANNADA,
        "English" to TranslateLanguage.ENGLISH,
        "Spanish" to TranslateLanguage.SPANISH,
        "French" to TranslateLanguage.FRENCH,
        "German" to TranslateLanguage.GERMAN,
        "Chinese" to TranslateLanguage.CHINESE,
        "Japanese" to TranslateLanguage.JAPANESE,
        "Korean" to TranslateLanguage.KOREAN,
        "Arabic" to TranslateLanguage.ARABIC,
        "Russian" to TranslateLanguage.RUSSIAN
    )

    private var currentTranslator: Translator? = null

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
        val langNames = languageMap.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, langNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLangName = langNames[position]
                val selectedLangCode = languageMap[selectedLangName] ?: TranslateLanguage.ENGLISH
                prepareTranslatorAndFetchData(selectedLangCode)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun prepareTranslatorAndFetchData(targetLangCode: String) {
        currentTranslator?.close()

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLangCode)
            .build()
        
        currentTranslator = Translation.getClient(options)
        
        txtPanchang.text = "ભાષા ડાઉનલોડ થઈ રહી છે..."
        
        currentTranslator?.downloadModelIfNeeded()
            ?.addOnSuccessListener { fetchSheetData() }
            ?.addOnFailureListener { e -> 
                txtPanchang.text = "Error: ${e.message}" 
            }
    }

    private fun fetchSheetData() {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val todayDate = sdf.format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { txtPanchang.text = "કનેક્શન એરર!" }
            }

            override fun onResponse(call: Call, response: Response) {
                val csvContent = response.body?.string() ?: ""
                val lines = csvContent.split("\n")

                for (line in lines) {
                    val row = line.split(",")
                    if (row.isNotEmpty() && row[0].contains(todayDate)) {
                        // ડેટા ભેગો કરો
                        val englishText = "Panchang: ${row[2]}\nIslamic: ${row[4]}"
                        
                        currentTranslator?.translate(englishText)
                            ?.addOnSuccessListener { translatedText ->
                                runOnUiThread {
                                    txtDate.text = "તારીખ: ${row[0]}"
                                    txtPanchang.text = translatedText
                                    if (row.size > 30) {
                                        currentTranslator?.translate(row[30])?.addOnSuccessListener {
                                            txtFestival.text = it
                                        }
                                    }
                                    if (row.size > 31) txtEmoji.text = row[31]
                                }
                            }
                        break
                    }
                }
            }
        })
    }
}

