package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.indian.calendar.model.CalendarDayData
import org.json.JSONArray
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val todayData = getTodayCalendarData(context)

        appWidgetIds.forEach { widgetId ->
            val views = RemoteViews(context.packageName, R.layout.calendar_widget)
            views.setTextViewText(R.id.line1, todayData.Date)
            views.setTextViewText(
                R.id.line2,
                "${todayData.Gujarati_Month} ${todayData.Tithi}, ${todayData.Day}"
            )
            views.setTextViewText(R.id.line3, todayData.Festival_English)
            views.setTextViewText(R.id.line4, "") // future special day, જો હોય તો set કરો

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private fun getTodayCalendarData(context: Context): CalendarDayData {
        val jsonStream: InputStream = context.assets.open("calendar.json")
        val jsonText = jsonStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonText)

        val today = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val todayStr = sdf.format(today.time)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            if (obj.getString("Date") == todayStr) {
                return CalendarDayData(
                    Date = obj.getString("Date"),
                    Gujarati_Month = obj.getString("Gujarati_Month"),
                    Tithi = obj.getString("Tithi"),
                    Day = obj.getString("Day"),
                    Festival_English = obj.optString("Festival_English", "")
                )
            }
        }

        return CalendarDayData(
            Date = todayStr,
            Gujarati_Month = "",
            Tithi = "",
            Day = "",
            Festival_English = ""
        )
    }
}
