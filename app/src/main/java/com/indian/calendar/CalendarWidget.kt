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

    // આ ફંક્શન અંગ્રેજી શબ્દોને ગુજરાતીમાં ફેરવશે
    private fun translateToLocal(text: String): String {
        val map = mapOf(
            "0" to "૦", "1" to "૧", "2" to "૨", "3" to "૩", "4" to "૪", "5" to "૫",
            "6" to "૬", "7" to "૭", "8" to "૮", "9" to "૯",
            "January" to "જાન્યુઆરી", "February" to "ફેબ્રુઆરી", "March" to "માર્ચ",
            "April" to "એપ્રિલ", "May" to "મે", "June" to "જૂન",
            "July" to "જુલાઈ", "August" to "ઓગસ્ટ", "September" to "સપ્ટેમ્બર",
            "October" to "ઓક્ટોબર", "November" to "નવેમ્બર", "December" to "ડિસેમ્બર",
            "Saturday" to "શનિવાર", "Sunday" to "રવિવાર", "Monday" to "સોમવાર",
            "Tuesday" to "મંગળવાર", "Wednesday" to "બુધવાર", "Thursday" to "ગુરુવાર",
            "Friday" to "શુક્રવાર",
            "Paush Sud" to "પોષ સુદ", "Paush Vad" to "પોષ વદ", "Paush Purnima" to "પોષ પૂનમ",
            "New Year" to "ખ્રિસ્તી નવું વર્ષ",
            "Putrada Ekadashi" to "પુત્રદા એકાદશી",
            "Global Family Day" to "વિશ્વ પરિવાર દિવસ"
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
            
            // સિસ્ટમની તારીખ અને વાર મેળવો
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val currentDate = sdf.format(calendar.time) // "2026-01-01"
            
            // વાર મેળવો (કારણ કે JSON માં નથી)
            val dayName = SimpleDateFormat("EEEE", Locale.US).format(calendar.time) // "Thursday"

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)
                    val calendars = dateData.getJSONObject("calendars")

                    // ૧. કાર્ડ ૧: 01, January - 2026, Thursday
                    val greg = calendars.getJSONObject("gregorian")
                    val commonDate = "${greg.getString("date")}, ${greg.getString("month")} - ${greg.getString("year")}, $dayName"
                    views.setTextViewText(R.id.widget_english_date, commonDate)

                    // ૨. કાર્ડ ૨: વિ.સં. ૨૦૮૨, પોષ સુદ-૧૨, ગુરુવાર
                    if (calendars.has(selectedKey)) {
                        val calObj = calendars.getJSONObject(selectedKey)
                        val prefix = if (selectedKey == "vikram_samvat") "વિ.સં. " else ""
                        val localText = "$prefix${translateToLocal(calObj.getString("year"))}, ${translateToLocal(calObj.getString("month"))}-${translateToLocal(calObj.getString("date"))}, ${translateToLocal(dayName)}"
                        views.setTextViewText(R.id.widget_date_text, localText)
                    }

                    // ૩. તહેવાર અને વિશેષ દિવસ
                    val eventList = mutableListOf<String>()
                    val festivals = dateData.optJSONArray("festivals")
                    if (festivals != null) {
                        for (i in 0 until festivals.length()) {
                            val f = festivals.getJSONObject(i)
                            if (f.getString("category") == selectedKey || f.getString("category") == "all") {
                                eventList.add(translateToLocal(f.getString("name")))
                            }
                        }
                    }
                    val specials = dateData.optJSONArray("special_days")
                    if (specials != null) {
                        for (i in 0 until specials.length()) {
                            val s = specials.getJSONObject(i)
                            eventList.add(translateToLocal(s.getString("name")))
                        }
                    }

                    if (eventList.isNotEmpty()) {
                        views.setViewVisibility(R.id.widget_festival_text, View.VISIBLE)
                        views.setTextViewText(R.id.widget_festival_text, eventList.joinToString(" | "))
                    } else {
                        views.setViewVisibility(R.id.widget_festival_text, View.GONE)
                    }

                    // ૪. રીમાઇન્ડર
                    val reminder = sharedPref.getString("reminder_$currentDate", "")
                    if (!reminder.isNullOrEmpty()) {
                        views.setViewVisibility(R.id.widget_reminder_text, View.VISIBLE)
                        views.setTextViewText(R.id.widget_reminder_text, reminder)
                    } else {
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

