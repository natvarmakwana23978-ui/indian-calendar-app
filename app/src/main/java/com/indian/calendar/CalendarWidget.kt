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
            
            // ટેસ્ટિંગ માટે ડિફોલ્ટ લખાણ
            views.setTextViewText(R.id.widget_date_text, "લોડ થઈ રહ્યું છે...")
            
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            val selectedKey = sharedPref.getString("selected_key", "Vikram_Samvat") ?: "Vikram_Samvat"
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonText)

                var found = false
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    if (obj.getString("Date") == currentDate) {
                        val localData = obj.getString(selectedKey)
                        val special = obj.getString("Special_Day")
                        
                        views.setTextViewText(R.id.widget_date_text, localData)
                        views.setTextViewText(R.id.widget_festival_text, if (special == "--") "" else special)
                        found = true
                        break
                    }
                }
                if (!found) views.setTextViewText(R.id.widget_date_text, "ડેટા નથી મળ્યો")
                
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "Error: ${e.message}")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

