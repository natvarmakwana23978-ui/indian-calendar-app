package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var txtDate: TextView
    private lateinit var txtPanchang: TextView
    private lateinit var txtFestival: TextView
    private lateinit var languageSpinner: Spinner

    // તમારી JSON/Sheet મુજબની ભાષાઓ
    private val languages = arrayOf("Gujarati", "Hindi", "Marathi", "Bengali", "Tamil", "English")
    
    // ML Kit Translator સેટઅપ
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.GUJARATI) // ડિફોલ્ટ ગુજરાતી
        .build()
    private var translator = Translation.getClient(options)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        languageSpinner = findViewById(R.id.languageSpinner)

        setupSpinner()
        
        // ભાષાના મોડેલ ડાઉનલોડ કરવા (એક વાર નેટની જરૂર પડશે)
        translator.downloadModelIfNeeded().addOnSuccessListener {
            fetchTodayPanchang(0) // મોડેલ તૈયાર થાય એટલે ડેટા લોડ કરો
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        languageSpinner.adapter = adapter
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                updateTranslator(languages[pos])
                fetchTodayPanchang(pos)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun updateTranslator(lang: String) {
        val target = when(lang) {
            "Gujarati" -> TranslateLanguage.GUJARATI
            "Hindi" -> TranslateLanguage.HINDI
            "Marathi" -> TranslateLanguage.MARATHI
            "Bengali" -> TranslateLanguage.BENGALI
            "Tamil" -> TranslateLanguage.TAMIL
            else -> TranslateLanguage.ENGLISH
        }
        val newOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(target)
            .build()
        translator = Translation.getClient(newOptions)
    }

    private fun fetchTodayPanchang(langIndex: Int) {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val today = sdf.format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        OkHttpClient().newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val lines = response.body?.string()?.split("\n") ?: return
                for (line in lines) {
                    val row = line.split(",")
                    if (row.isNotEmpty() && row[0].contains(today)) {
                        val rawData = row[langIndex + 2] // અંગ્રેજી ડેટા
                        
                        // અંગ્રેજી માંથી પસંદ કરેલી ભાષામાં અનુવાદ
                        translator.translate(rawData).addOnSuccessListener { translatedText ->
                            runOnUiThread {
                                txtDate.text = "તારીખ: ${row[0]}"
                                txtPanchang.text = translatedText
                                if (row.size > 30) txtFestival.text = row[30]
                            }
                        }
                        break
                    }
                }
            }
        })
    }
}

