package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
            
            // ૧. અંગ્રેજી તારીખ, વાર, મહિનો
            val engFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH)
            views.setTextViewText(R.id.widget_english_date, engFormat.format(Date()))

            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            val selectedKey = sharedPref.getString("selected_key", "Vikram_Samvat") ?: "Vikram_Samvat"
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonText)

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    if (obj.getString("Date") == currentDate) {
                        // ૨. યુઝર ચોઈસ ડેટા
                        views.setTextViewText(R.id.widget_date_text, obj.getString(selectedKey))

                        // ૩. તહેવારો + વિશેષ દિવસ
                        val special = obj.getString("Special_Day")
                        views.setTextViewText(R.id.widget_festival_text, if (special == "--") "" else special)

                        // ૪. રીમાઇન્ડર (જો એપમાં સેટ કર્યા હોય તો, હાલ સેમ્પલ ટેક્સ્ટ)
                        val reminder = sharedPref.getString("reminder_$currentDate", "")
                        views.setTextViewText(R.id.widget_reminder_text, reminder)
                        
                        break
                    }
                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "ડેટા લોડ એરર")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
