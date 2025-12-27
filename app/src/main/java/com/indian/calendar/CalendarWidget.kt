package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
            
            // ૧. અંગ્રેજી તારીખ અને વાર (ઉપરની લાઇન માટે)
            val engFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH)
            views.setTextViewText(R.id.widget_english_date, engFormat.format(Date()))

            // ૨. સેટિંગ્સમાંથી યુઝરની પસંદગી અને આજની તારીખ મેળવો
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            // જો યુઝરે કઈ પસંદ ન કર્યું હોય તો 'vikram_gu' ડિફોલ્ટ દેખાશે
            val selectedKey = sharedPref.getString("selected_key", "vikram_gu") ?: "vikram_gu"
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

            try {
                // ૩. JSON ફાઇલ વાંચવી
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                // ૪. જો આજની તારીખનો ડેટા JSON માં હોય તો
                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)

                    // લોકલ કેલેન્ડરની વિગત (બીજી લાઇન)
                    val calendars = dateData.getJSONObject("calendars")
                    val localDateValue = if (calendars.has(selectedKey)) calendars.getString(selectedKey) else ""
                    views.setTextViewText(R.id.widget_date_text, localDateValue)

                    // તહેવાર અને વિશેષ દિવસ (ત્રીજી લાઇન)
                    val festivals = dateData.getJSONObject("festivals")
                    val festName = if (festivals.has("gu")) festivals.getString("gu") else ""
                    
                    val specialDays = dateData.getJSONObject("special_day")
                    val specialName = if (specialDays.has("gu")) specialDays.getString("gu") else ""

                    // બંને વિગતોને એકસાથે બતાવવી
                    val eventText = when {
                        festName.isNotEmpty() && specialName.isNotEmpty() -> "$festName | $specialName"
                        festName.isNotEmpty() -> festName
                        else -> specialName
                    }
                    views.setTextViewText(R.id.widget_festival_text, eventText)

                    // યુઝરનું પર્સનલ રીમાઇન્ડર (ચોથી લાઇન)
                    val reminder = sharedPref.getString("reminder_$currentDate", "")
                    views.setTextViewText(R.id.widget_reminder_text, reminder)
                }
            } catch (e: Exception) {
                // જો કોઈ ભૂલ આવે તો
                views.setTextViewText(R.id.widget_date_text, "ડેટા લોડ થઈ શક્યો નથી")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

