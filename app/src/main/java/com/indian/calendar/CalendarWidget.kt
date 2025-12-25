package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.indian.calendar.R  // આ લાઇન સૌથી મહત્વની છે
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)

        thread {
            try {
                val jsonUrl = "https://raw.githubusercontent.com/natvarmakwana23978-ui/indian-calendar-app/main/app/src/main/assets/json/calendar_2082.json"
                val url = URL(jsonUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                connection.connectTimeout = 15000

                val jsonText = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonText)

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = sdf.format(Date())

                var found = false
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    if (obj.getString("Date") == currentDate) {
                        views.setTextViewText(R.id.widget_date_text, obj.getString("Vikram_Samvat"))
                        val special = obj.getString("Special_Day")
                        views.setTextViewText(R.id.widget_festival_text, if (special == "--") "" else special)
                        found = true
                        break
                    }
                }

                if (!found) {
                    views.setTextViewText(R.id.widget_date_text, "ડેટા નથી ($currentDate)")
                }
                appWidgetManager.updateAppWidget(appWidgetId, views)

            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "લોડિંગ ભૂલ...")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
