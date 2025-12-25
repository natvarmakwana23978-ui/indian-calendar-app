package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.indian.calendar.R
import org.json.JSONArray
import java.net.URL
import kotlin.concurrent.thread

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidgetData(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidgetData(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)
        
        // તમારી સાચી GitHub JSON Raw Link
        val jsonUrl = "https://raw.githubusercontent.com/natvarmakwana23978-ui/indian-calendar-app/main/app/src/main/assets/json/calendar_2082.json"

        thread {
            try {
                // નેટવર્ક પરથી JSON ડેટા મેળવવો
                val rawJson = URL(jsonUrl).readText()
                val jsonArray = JSONArray(rawJson)
                
                // હાલ પૂરતું જાન્યુઆરી ૧, ૨૦૨૬ નો ડેટા (Index 0) લઈએ છીએ
                val data = jsonArray.getJSONObject(0)

                // JSON માંથી સાચી Keys મુજબ ડેટા સેટ કરવો
                views.setTextViewText(R.id.txt_eng_date, data.optString("Date"))
                views.setTextViewText(R.id.txt_event_top, data.optString("Special_Day"))
                views.setTextViewText(R.id.txt_event_note, "Saka: " + data.optString("Saka"))
                
                views.setTextViewText(R.id.txt_guj_tithi, data.optString("Vikram_Samvat"))
                views.setTextViewText(R.id.txt_guj_event, data.optString("Special_Day"))
                views.setTextViewText(R.id.txt_birthday_wish, "જેમીની ને જન્મ દિવસ ની ખૂબ ખૂબ શુભેચ્છા")

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                // જો કોઈ ભૂલ આવે તો વિજેટમાં એરર મેસેજ બતાવવો
                views.setTextViewText(R.id.txt_event_top, "ડેટા લોડ થયો નથી")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
