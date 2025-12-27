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
            "New Year" -> "ખ્રિસ્તી નવું વર્ષ",
            "Putrada Ekadashi" -> "પુત્રદા એકાદશી",
            "Global Family Day" -> "વિશ્વ પરિવાર દિવસ"
        )
        var result = text
        map.forEach { (eng, local) -> result = result.replace(eng, local) }
        return result
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            
            // સિલેક્શન લોડ કરો
            val selectedKey = sharedPref.getString("selected_key", "vikram_samvat") ?: "vikram_samvat"
            
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val currentDate = sdf.format(calendar.time) 
            val dayName = SimpleDateFormat("EEEE", Locale.US).format(calendar.time)

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)
                    val calendars = dateData.getJSONObject("calendars")

                    // ૧. કાર્ડ ૧ (Gregorian)
                    val greg = calendars.getJSONObject("gregorian")
                    val commonDate = "${greg.getString("date")}, ${greg.getString("month")} - ${greg.getString("year")}, $dayName"
                    views.setTextViewText(R.id.widget_english_date, commonDate)

                    // ૨. કાર્ડ ૨ (Selected Calendar)
                    if (calendars.has(selectedKey)) {
                        val calObj = calendars.getJSONObject(selectedKey)
                        val prefix = when(selectedKey) {
                            "vikram_samvat" -> "વિ.સં. "
                            "shaka_samvat" -> "શક સંવત "
                            "hijri" -> "હિજરી "
                            else -> ""
                        }
                        val localText = "$prefix${translateToLocal(calObj.getString("year"))}, ${translateToLocal(calObj.getString("month"))} - ${translateToLocal(calObj.getString("date"))}, ${translateToLocal(dayName)}"
                        views.setTextViewText(R.id.widget_date_text, localText)
                        views.setViewVisibility(R.id.widget_date_text, View.VISIBLE)
                    }

                    // ૩. તહેવાર અને વિશેષ દિવસ
                    val eventList = mutableListOf<String>()
                    val festivals = dateData.optJSONArray("festivals")
                    if (festivals != null) {
                        for (i in 0 until festivals.length()) {
                            val f = festivals.getJSONObject(i)
                            if (f.getString("category") == selectedKey || f.getString("category") == "all" || f.getString("category") == "gregorian") {
                                eventList.add(translateToLocal(f.getString("name")))
                            }
                        }
                    }
                    val specials = dateData.optJSONArray("special_days")
                    if (specials != null) {
                        for (i in 0 until specials.length()) {
                            eventList.add(translateToLocal(specials.getJSONObject(i).getString("name")))
                        }
                    }

                    if (eventList.isNotEmpty()) {
                        views.setViewVisibility(R.id.widget_festival_text, View.VISIBLE)
                        views.setTextViewText(R.id.widget_festival_text, eventList.distinct().joinToString(" | "))
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
                views.setTextViewText(R.id.widget_date_text, "ડેટા એરર")
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

