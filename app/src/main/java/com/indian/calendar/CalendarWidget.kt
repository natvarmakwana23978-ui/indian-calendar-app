package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.indian.calendar.model.CalendarDayData
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val calendarData = loadCalendarData(context)
        val today = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
        val todayData = calendarData.find { it.Date == today } ?: calendarData.first()
        val lines = getWidgetLines(todayData)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.calendar_widget)
            views.setTextViewText(R.id.line1, lines[0])
            views.setTextViewText(R.id.line2, lines[1])
            views.setTextViewText(R.id.line3, lines[2])
            views.setTextViewText(R.id.line4, lines[3])
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun loadCalendarData(context: Context): List<CalendarDayData> {
        val jsonString = context.assets.open("calendar_data.json")
            .bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<List<CalendarDayData>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    private fun getWidgetLines(dayData: CalendarDayData): List<String> {
        val firstLine = "${dayData.Date}, ${dayData.Day}"
        val secondLine = "${dayData.Gujarati_Month} ${dayData.Tithi}, ${dayData.Day}"
        val thirdLine = dayData.Festival_English.ifEmpty { "" }

        // Line 4: Special events auto
        val specialDays = mutableListOf<String>()
        val monthDay = dayData.Date.takeLast(5) // MM/dd

        if (monthDay == "03/08") specialDays.add("Women's Day")
        if (dayData.Festival_English.contains("Ekadashi", ignoreCase = true)) specialDays.add("Ekadashi")
        if (dayData.Festival_English.contains("Purnima", ignoreCase = true)) specialDays.add("Purnima")
        if (dayData.Festival_English.contains("Amavasya", ignoreCase = true)) specialDays.add("Amavasya")

        val fourthLine = specialDays.joinToString(", ")
        return listOf(firstLine, secondLine, thirdLine, fourthLine)
    }
}
