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

    private fun translateToLocal(text: String): String {
        val map = mapOf(
            "0" to "૦", "1" to "૧", "2" to "૨", "3" to "૩", "4" to "૪", "5" to "૫", 
            "6" to "૬", "7" to "૭", "8" to "૮", "9" to "૯",
            "January" to "January", "February" to "February", "March" to "March",
            "Saturday" to "શનિવાર", "Sunday" to "રવિવાર", "Monday" to "સોમવાર",
            "Tuesday" to "મંગળવાર", "Wednesday" to "બુધવાર", "Thursday" to "ગુરુવાર",
            "Friday" to "શુક્રવાર",
            "Paush Sud" to "પોષ સુદ", "Paush Vad" to "પોષ વદ", "Paush Purnima" to "પોષ પૂનમ",
            "vikram_samvat" to "વિ.સં."
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
            
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)
                    val calendars = dateData.getJSONObject("calendars")

                    // ૧. કાર્ડ ૧: 01, January - 2026, Thursday
                    val greg = calendars.getJSONObject("gregorian")
                    val commonDate = "${greg.getString("date")}, ${greg.getString("month")} - ${greg.getString("year")}, ${greg.getString("day")}"
                    views.setTextViewText(R.id.widget_english_date, commonDate)

                    // ૨. કાર્ડ ૨: વિ.સં. ૨૦૮૨, પોષ સુદ-૧૨, ગુરુવાર
                    val calObj = calendars.getJSONObject(selectedKey)
                    val prefix = if (selectedKey == "vikram_samvat") "વિ.સં. " else ""
                    val localText = "$prefix${translateToLocal(calObj.getString("year"))}, ${translateToLocal(calObj.getString("month"))}-${translateToLocal(calObj.getString("date"))}, ${translateToLocal(greg.getString("day"))}"
                    views.setTextViewText(R.id.widget_date_text, localText)

                    // ૩. તહેવાર અને વિશેષ દિવસ (Flexible Logic)
                    val festivals = dateData.getJSONArray("festivals")
                    val specials = dateData.getJSONArray("special_days")
                    
                    var festName = ""
                    for (i in 0 until festivals.length()) {
                        val f = festivals.getJSONObject(i)
                        if (f.getString("category") == selectedKey || f.getString("category") == "all") {
                            festName = f.getString("name")
                            break
                        }
                    }

                    var specialName = ""
                    for (i in 0 until specials.length()) {
                        val s = specials.getJSONObject(i)
                        if (s.getString("category") == "all") {
                            specialName = s.getString("name")
                            break
                        }
                    }

                    // જો ડેટા હોય તો જ બતાવો, નહીતર View છુપાવી દો (Flexible)
                    if (festName.isNotEmpty()) {
                        views.setViewVisibility(R.id.widget_festival_text, View.VISIBLE)
                        views.setTextViewText(R.id.widget_festival_text, festName)
                    } else {
                        views.setViewVisibility(R.id.widget_festival_text, View.GONE)
                    }

                    if (specialName.isNotEmpty()) {
                        views.setViewVisibility(R.id.widget_special_day_text, View.VISIBLE) // આ નવું ID લેઆઉટમાં હોવું જોઈએ
                        views.setTextViewText(R.id.widget_special_day_text, specialName)
                    } else {
                        views.setViewVisibility(R.id.widget_special_day_text, View.GONE)
                    }

                    // ૪. રીમાઇન્ડર
                    val reminder = sharedPref.getString("reminder_$currentDate", "")
                    if (!reminder.isNullOrEmpty()) {
                        views.setViewVisibility(R.id.widget_reminder_text, View.VISIBLE)
                        views.setTextViewText(R.id.widget_reminder_text, reminder)
                    } else {
                        // જો રીમાઇન્ડર ખાલી હોય તો સુવિચાર બતાવો અથવા છુપાવો
                        views.setViewVisibility(R.id.widget_reminder_text, View.GONE)
                    }
                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "ડેટા લોડ એરર")
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

