package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)

        // ૧. કેલેન્ડર સ્પિનર
        val calSpinner: Spinner = findViewById(R.id.calendar_spinner)
        val calOptions = arrayOf("વિક્રમ સંવત", "શક સંવત", "હિજરી", "નાનકશાહી", "ચાઈનીઝ", "બૌદ્ધ", "પારસી")
        val calKeys = arrayOf("vikram_samvat", "shaka_samvat", "hijri", "nanakshahi", "chinese", "buddhist", "persian")
        
        calSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calOptions)

        // ૨. ગ્લોબલ ભાષા લિસ્ટ (વિશ્વની તમામ મુખ્ય ભાષાઓ)
        val langSpinner: Spinner = findViewById(R.id.language_spinner)
        
        // ભાષાના નામ અને તેના ML Kit કોડની જોડી
        val langMap = linkedMapOf(
            "English" to TranslateLanguage.ENGLISH,
            "ગુજરાતી" to TranslateLanguage.GUJARATI,
            "Hindi (हिन्दी)" to TranslateLanguage.HINDI,
            "Marathi (मराठी)" to TranslateLanguage.MARATHI,
            "Bengali (বাংলা)" to TranslateLanguage.BENGALI,
            "Tamil (தமிழ்)" to TranslateLanguage.TAMIL,
            "Telugu (తెలుగు)" to TranslateLanguage.TELUGU,
            "Kannada (ಕನ್ನಡ)" to TranslateLanguage.KANNADA,
            "Malayalam (മലയാളം)" to TranslateLanguage.MALAYALAM,
            "Spanish (Español)" to TranslateLanguage.SPANISH,
            "French (Français)" to TranslateLanguage.FRENCH,
            "German (Deutsch)" to TranslateLanguage.GERMAN,
            "Russian (Русский)" to TranslateLanguage.RUSSIAN,
            "Japanese (日本語)" to TranslateLanguage.JAPANESE,
            "Chinese (中文)" to TranslateLanguage.CHINESE,
            "Arabic (العربية)" to TranslateLanguage.ARABIC,
            "Hebrew (עברית)" to TranslateLanguage.HEBREW,
            "Portuguese (Português)" to TranslateLanguage.PORTUGUESE,
            "Korean (한국어)" to TranslateLanguage.KOREAN,
            "Italian (Italiano)" to TranslateLanguage.ITALIAN,
            "Turkish (Türkçe)" to TranslateLanguage.TURKISH,
            "Dutch (Nederlands)" to TranslateLanguage.DUTCH,
            "Polish (Polski)" to TranslateLanguage.POLISH,
            "Indonesian (Bahasa)" to TranslateLanguage.INDONESIAN,
            "Vietnamese (Tiếng Việt)" to TranslateLanguage.VIETNAMESE,
            "Thai (ไทย)" to TranslateLanguage.THAI
        )

        val langNames = langMap.keys.toTypedArray()
        val langCodes = langMap.values.toTypedArray()

        langSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, langNames)

        // લોડ સેવ્ડ ડેટા
        val savedCal = sharedPref.getString("selected_key", "vikram_samvat")
        calSpinner.setSelection(calKeys.indexOf(savedCal).coerceAtLeast(0))

        val savedLang = sharedPref.getString("selected_language", TranslateLanguage.ENGLISH)
        langSpinner.setSelection(langCodes.indexOf(savedLang).coerceAtLeast(0))

        // ઈવેન્ટ્સ
        calSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                sharedPref.edit().putString("selected_key", calKeys[pos]).apply()
                notifyWidgetUpdate()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        langSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val selectedLang = langCodes[pos]
                sharedPref.edit().putString("selected_language", selectedLang).apply()
                
                // ઓફલાઇન ટ્રાન્સલેશન માટે મોડેલ ડાઉનલોડ કરો
                prepareTranslator(selectedLang)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun prepareTranslator(targetLang: String) {
        if (targetLang == TranslateLanguage.ENGLISH) {
            notifyWidgetUpdate()
            return
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLang)
            .build()
        
        val translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder().build() // વાઈફાઈ કે મોબાઈલ ડેટા ગમે તે ચાલશે
        
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                notifyWidgetUpdate() // ડાઉનલોડ પૂરું થાય એટલે વિજેટ અપડેટ કરો
            }
            .addOnFailureListener {
                Toast.makeText(this, "ഭാષા ડાઉનલોડમાં સમસ્યા છે.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun notifyWidgetUpdate() {
        val intent = Intent(this, CalendarWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
            ComponentName(application, CalendarWidget::class.java)
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}

