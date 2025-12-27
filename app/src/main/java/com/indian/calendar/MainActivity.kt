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
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.calendar_spinner)
        val sharedPref = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)

        // કેલેન્ડરના વિકલ્પો અને તેની Key
        val calendarOptions = arrayOf("વિક્રમ સંવત", "શક સંવત", "હિજરી", "નાનકશાહી", "ચાઈનીઝ")
        val calendarKeys = arrayOf("vikram_samvat", "shaka_samvat", "hijri", "nanakshahi", "chinese")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendarOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // અગાઉનું સિલેક્શન લોડ કરો
        val savedKey = sharedPref.getString("selected_key", "vikram_samvat")
        val savedIndex = calendarKeys.indexOf(savedKey)
        if (savedIndex != -1) {
            spinner.setSelection(savedIndex)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedKey = calendarKeys[position]
                
                // ડેટા સેવ કરો (commit વાપરીને જેથી તરત લખાય)
                val editor = sharedPref.edit()
                editor.putString("selected_key", selectedKey)
                editor.commit()

                // વિજેટને તરત અપડેટ કરવા માટેનો આદેશ
                val intent = Intent(this@MainActivity, CalendarWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
                    ComponentName(application, CalendarWidget::class.java)
                )
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                sendBroadcast(intent)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}

