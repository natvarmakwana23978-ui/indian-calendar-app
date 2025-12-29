package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)

        // ૧. ભાષા પસંદગી માટે Spinner સેટઅપ
        val spinnerLanguage = findViewById<Spinner>(R.id.spinnerLanguage)
        val languages = arrayOf("gu", "hi", "en", "fr", "es", "ar", "ja")
        val langAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = langAdapter

        // ૨. કેલેન્ડર પસંદગી માટે Spinner સેટઅપ
        val spinnerCalendar = findViewById<Spinner>(R.id.spinnerCalendar)
        val calendars = arrayOf("islamic", "persian", "hebrew", "indian", "chinese", "ethiopic")
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendars)
        calAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCalendar.adapter = calAdapter

        // ૩. Switch સેટઅપ (તહેવાર અને વિશેષ દિવસ)
        val switchAllFestivals = findViewById<SwitchCompat>(R.id.switchAllFestivals)
        val switchSpecialDays = findViewById<SwitchCompat>(R.id.switchSpecialDays)

        // અગાઉ સેવ કરેલી વેલ્યુ સેટ કરવી (જેથી એપ ખોલતા જુનું સેટિંગ દેખાય)
        val savedLang = prefs.getString("language", "gu")
        spinnerLanguage.setSelection(languages.indexOf(savedLang))
        
        val savedCal = prefs.getString("calendar_type", "islamic")
        spinnerCalendar.setSelection(calendars.indexOf(savedCal))

        switchAllFestivals.isChecked = prefs.getBoolean("show_all_festivals", false)
        switchSpecialDays.isChecked = prefs.getBoolean("show_special_days", true)

        // ૪. સેવ બટન પર ક્લિક ઇવેન્ટ
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val editor = prefs.edit()
            
            // ડેટા સેવ કરવો
            editor.putString("language", spinnerLanguage.selectedItem.toString())
            editor.putString("calendar_type", spinnerCalendar.selectedItem.toString())
            editor.putBoolean("show_all_festivals", switchAllFestivals.isChecked)
            editor.putBoolean("show_special_days", switchSpecialDays.isChecked)
            editor.apply()

            // વિજેટને તરત અપડેટ કરવા માટેનો આદેશ
            updateWidget(this)
            
            // યુઝરને જાણ કરવી (ઓપ્શનલ)
            android.widget.Toast.makeText(this, "સેટિંગ્સ સેવ થયા!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    // વિજેટને ફોર્સફુલી અપડેટ કરવાનું ફંક્શન
    private fun updateWidget(context: Context) {
        val intent = Intent(context, CalendarWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, CalendarWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
}

