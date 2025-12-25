package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.indian.calendar.R
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)

        try {
            // મોબાઇલમાં ઇન્સ્ટોલ થયેલી એસેટ્સ ફાઇલ ખોલવા માટે
            val inputStream = context.assets.open("json/calendar_2082.json")
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonText)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())

            var found = false
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.getString("Date") == currentDate) {
                    views.setTextViewText(R.id.widget_date_text, obj.getString("Vikram_Samvat"))
                    val special = obj.getString("Special_Day")
                    views.setTextViewText(R.id.widget_festival_text, if (special == "--") "" else special)
                    found = true
                    break
                }
            }

            if (!found) {
                views.setTextViewText(R.id.widget_date_text, "ડેટા નથી ($currentDate)")
                views.setTextViewText(R.id.widget_festival_text, "")
            }

        } catch (e: Exception) {
            views.setTextViewText(R.id.widget_date_text, "ફાઇલ મળી નથી!")
            views.setTextViewText(R.id.widget_festival_text, "Assets ચેક કરો")
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
