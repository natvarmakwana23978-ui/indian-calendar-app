package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log

class CalendarWidget : AppWidgetProvider() {

    private fun translateToLocal(text: String): String {
        val map = mapOf(
            "0" to "૦", "1" to "૧", "2" to "૨", "3" to "૩", "4" to "૪", "5" to "૫", 
            "6" to "૬", "7" to "૭", "8" to "૮", "9" to "૯",
            "January" to "જાન્યુઆરી", "February" to "ફેબ્રુઆરી", "March" to "માર્ચ",
            "Saturday" to "શનિવાર", "Sunday" to "રવિવાર", "Monday" to "સોમવાર",
            "Tuesday" to "મંગળવાર", "Wednesday" to "બુધવાર", "Thursday" to "ગુરુવાર",
            "Friday" to "શુક્રવાર",
            "Paush Sud" to "પોષ સુદ", "Paush Vad" to "પોષ વદ", "Paush Purnima" to "પોષ પૂનમ"
        )
        var result = text
        map.forEach { (eng, local) -> result = result.replace(eng, local) }
        return result
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            val selectedKey = sharedPref.getString("selected_key", "vikram_samvat") ?: "vikram_samvat"
            
            // ટેસ્ટિંગ માટે આપણે સીધી તારીખ પણ લખી શકીએ જો સિસ્ટમ ડેટ ન મળતી હોય
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val currentDate = sdf.format(Date()) 

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                // તપાસો કે જેસોનમાં આ તારીખની 'Key' છે કે નહીં
                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)
                    val calendars = dateData.getJSONObject("calendars")

                    // ૧. કાર્ડ ૧ (Gregorian)
                    val greg = calendars.getJSONObject("gregorian")
                    val commonDate = "${greg.getString("date")}, ${greg.getString("month")} - ${greg.getString("year")}, ${translateToLocal(greg.getString("day"))}"
                    views.setTextViewText(R.id.widget_english_date, commonDate)

                    // ૨. કાર્ડ ૨ (Local)
                    if (calendars.has(selectedKey)) {
                        val calObj = calendars.getJSONObject(selectedKey)
                        val prefix = if (selectedKey == "vikram_samvat") "વિ.સં. " else ""
                        val localText = "$prefix${translateToLocal(calObj.getString("year"))}, ${translateToLocal(calObj.getString("month"))}-${translateToLocal(calObj.getString("date"))}, ${translateToLocal(greg.getString("day"))}"
                        views.setTextViewText(R.id.widget_date_text, localText)
                    }

                    // ૩. તહેવારો
                    val festivals = dateData.optJSONArray("festivals")
                    val eventList = mutableListOf<String>()
                    if (festivals != null) {
                        for (i in 0 until festivals.length()) {
                            val f = festivals.getJSONObject(i)
                            if (f.getString("category") == selectedKey || f.getString("category") == "all") {
                                eventList.add(f.getString("name"))
                            }
                        }
                    }
                    
                    if (eventList.isNotEmpty()) {
                        views.setViewVisibility(R.id.widget_festival_text, View.VISIBLE)
                        views.setTextViewText(R.id.widget_festival_text, eventList.joinToString(" | "))
                    } else {
                        views.setViewVisibility(R.id.widget_festival_text, View.GONE)
                    }

                } else {
                    // જો જેસોનમાં આજની તારીખ ન હોય (દા.ત. આજે ૨૭ ડિસેમ્બર ૨૦૨૫ છે અને જેસોન ૨૦૨૬ થી શરૂ થાય છે)
                    views.setTextViewText(R.id.widget_date_text, "તારીખ: $currentDate જેસોનમાં નથી")
                }

            } catch (e: Exception) {
                Log.e("WIDGET_ERROR", e.message ?: "Unknown Error")
                views.setTextViewText(R.id.widget_date_text, "ફાઈલ લોડ એરર")
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

