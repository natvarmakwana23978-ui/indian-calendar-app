package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    // આ ફંક્શન હવે આખા શબ્દોનું ભાષાંતર કરશે
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
            
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)
                    val calendars = dateData.getJSONObject("calendars")

                    // ૧. કોમન તારીખ (માળખું: 01 January, Thursday)
                    val greg = calendars.getJSONObject("gregorian")
                    val commonDate = "${greg.getString("date")} ${greg.getString("month")}, ${greg.getString("day")}"
                    views.setTextViewText(R.id.widget_english_date, commonDate)

                    // ૨. યુઝરનું કેલેન્ડર (માળખું: પોષ સુદ ૧૨, શનિવાર)
                    val calObj = calendars.getJSONObject(selectedKey)
                    val localText = "${translateToLocal(calObj.getString("month"))} ${translateToLocal(calObj.getString("date"))}, ${translateToLocal(greg.getString("day"))}"
                    views.setTextViewText(R.id.widget_date_text, localText)

                    // ૩. તહેવાર અને વિશેષ દિવસ
                    val festivals = dateData.getJSONArray("festivals")
                    val specials = dateData.getJSONArray("special_days")
                    var eventLine = ""
                    
                    for (i in 0 until festivals.length()) {
                        val f = festivals.getJSONObject(i)
                        if (f.getString("category") == selectedKey || f.getString("category") == "all") {
                            eventLine = f.getString("name")
                            break
                        }
                    }
                    
                    for (i in 0 until specials.length()) {
                        val s = specials.getJSONObject(i)
                        if (s.getString("category") == "all") {
                            eventLine += if (eventLine.isEmpty()) s.getString("name") else " | ${s.getString("name")}"
                            break
                        }
                    }
                    views.setTextViewText(R.id.widget_festival_text, eventLine)

                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "ડેટા એરર")
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

