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
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)

            // ૧. એપમાંથી યુઝરની પસંદગી મેળવો (ડિફોલ્ટ: Vikram_Samvat)
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            val selectedKey = sharedPref.getString("selected_key", "Vikram_Samvat") ?: "Vikram_Samvat"

            // ૨. આજની અંગ્રેજી તારીખ અને વાર (તમારા સૂચન મુજબ)
            val lang = if (selectedKey == "Vikram_Samvat") Locale("gu") else Locale.US
            val sdfEnglish = SimpleDateFormat("dd MMM yyyy (EEEE)", lang)
            val englishDateStr = sdfEnglish.format(Date())

            try {
                // ૩. JSON માંથી ડેટા વાંચો
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonText)
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

                var found = false
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    if (obj.getString("Date") == currentDate) {
                        
                        // લાઈન ૧: અંગ્રેજી તારીખ + લોકલ કેલેન્ડર ડેટા
                        var localData = obj.getString(selectedKey)
                        if (selectedKey != "Vikram_Samvat") {
                            localData = localData.replace("૧", "1").replace("૨", "2").replace("૩", "3")
                                .replace("૪", "4").replace("૫", "5").replace("૬", "6")
                                .replace("૭", "7").replace("૮", "8").replace("૯", "9").replace("૦", "0")
                        }
                        
                        // વિજેટમાં ઉપર અંગ્રેજી તારીખ અને નીચે લોકલ તારીખ
                        views.setTextViewText(R.id.widget_date_text, "$englishDateStr\n$localData")

                        // લાઈન ૨: તહેવાર
                        val special = obj.getString("Special_Day")
                        views.setTextViewText(R.id.widget_festival_text, if (special == "--") "" else special)

                        // લાઈન ૩: રીમાઇન્ડર (તમારો સ્ટેટિક ડેટા અથવા એપ માંથી સેટ કરેલો)
                        views.setTextViewText(R.id.widget_reminder_text, "રીમાઇન્ડર: ડેટા ટેસ્ટિંગ શરૂ છે")
                        
                        found = true
                        break
                    }
                }
                if (!found) views.setTextViewText(R.id.widget_date_text, englishDateStr)

            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "Error loading data")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

