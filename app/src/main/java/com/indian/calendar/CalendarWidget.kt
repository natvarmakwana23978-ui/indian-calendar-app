package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())
            val displayDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(Date())
            
            try {
                // તમારા assets/json/ ફોલ્ડર માંથી ફાઈલ લોડ કરશે
                val jsonString = context.assets.open("json/calendar_2082_global.json").bufferedReader().use { it.readText() }
                val root = JSONObject(jsonString)
                
                if (root.has(today)) {
                    val data = root.getJSONObject(today)
                    val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
                    val calKey = prefs.getString("selected_calendar", "islamic") ?: "islamic"
                    
                    views.setTextViewText(R.id.txtLine1, displayDate)
                    
                    val globalCals = data.getJSONObject("global_calendars")
                    views.setTextViewText(R.id.txtLine2, globalCals.optString(calKey, "N/A"))

                    val fest = data.getJSONObject("gujarati_info").optString("festival")
                    val spec = data.optString("special_day")
                    val line3 = listOf(fest, spec).filter { it.isNotEmpty() }.joinToString(" | ")

                    if (line3.isNotEmpty()) {
                        views.setViewVisibility(R.id.txtLine3, View.VISIBLE)
                        views.setTextViewText(R.id.txtLine3, line3)
                    } else {
                        views.setViewVisibility(R.id.txtLine3, View.GONE)
                    }
                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.txtLine1, "ડેટા લોડ કરવામાં ભૂલ")
            }

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}

