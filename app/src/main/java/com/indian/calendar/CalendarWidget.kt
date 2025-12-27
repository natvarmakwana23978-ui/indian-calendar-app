package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    private fun toGujaratiNumbers(input: String): String {
        val englishNumbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val gujaratiNumbers = arrayOf("૦", "૧", "૨", "૩", "૪", "૫", "૬", "૭", "૮", "૯")
        var result = input
        for (i in 0..9) { result = result.replace(englishNumbers[i], gujaratiNumbers[i]) }
        return result
    }

    private fun translateToGuj(text: String): String {
        return when (text) {
            "January" -> "જાન્યુઆરી"
            "Paush Sud" -> "પોષ સુદ"
            "Paush Vad" -> "પોષ વદ"
            "Paush Purnima" -> "પોષ પૂનમ"
            "Saturday" -> "શનિવાર"
            "Thursday" -> "ગુરુવાર"
            "Friday" -> "શુક્રવાર"
            "Sunday" -> "રવિવાર"
            "Monday" -> "સોમવાર"
            "Tuesday" -> "મંગળવાર"
            "Wednesday" -> "બુધવાર"
            else -> text
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            val selectedKey = sharedPref.getString("selected_key", "vikram_samvat") ?: "vikram_samvat"
            
            // આજની તારીખ (JSON માં શોધવા માટે)
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)
                    val calendars = dateData.getJSONObject("calendars")

                    // ૧. કોમન ગ્રેગોરિયન (27 Dec, Sat)
                    val greg = calendars.getJSONObject("gregorian")
                    val commonDate = "${toGujaratiNumbers(greg.getString("date"))} ${translateToGuj(greg.getString("month").substring(0, 3))}, ${translateToGuj(greg.getString("day")).substring(0, 3)}"
                    views.setTextViewText(R.id.widget_english_date, commonDate)

                    // ૨. યુઝર્સનું કેલેન્ડર (પોષ સુદ ૧૨, શનિ)
                    val calObj = calendars.getJSONObject(selectedKey)
                    val localDisplay = "${translateToGuj(calObj.getString("month"))} ${toGujaratiNumbers(calObj.getString("date"))}, ${translateToGuj(greg.getString("day")).substring(0, 3)}"
                    views.setTextViewText(R.id.widget_date_text, localDisplay)

                    // ૩. તહેવાર ફિલ્ટરિંગ
                    val festArray = dateData.getJSONArray("festivals")
                    var eventText = ""
                    for (i in 0 until festArray.length()) {
                        val f = festArray.getJSONObject(i)
                        if (f.getString("category") == selectedKey || f.getString("category") == "all") {
                            eventText = f.getString("name")
                            break
                        }
                    }
                    views.setTextViewText(R.id.widget_festival_text, eventText)

                    // ૪. રીમાઇન્ડર
                    val reminder = sharedPref.getString("reminder_$currentDate", "")
                    views.setTextViewText(R.id.widget_reminder_text, reminder)
                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "ડેટા એરર")
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

