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

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val todayData = getTodayCalendarData(context)
        appWidgetIds.forEach { widgetId ->
            val views = RemoteViews(context.packageName, R.layout.calendar_widget)
            
            // તમારી નવી શીટના હેડર મુજબ ENGLISH (તારીખ) અને Gujarati (તિથિ/તહેવાર) નો ઉપયોગ
            views.setTextViewText(R.id.line1, todayData.Date) 
            views.setTextViewText(R.id.line2, todayData.Gujarati) 
            
            // લાઈન ૩ અને ૪ ખાલી રાખીએ છીએ જેથી એરર ન આવે
            views.setTextViewText(R.id.line3, "") 
            views.setTextViewText(R.id.line4, "") 

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private fun getTodayCalendarData(context: Context): CalendarDayData {
        return try {
            val jsonStream: InputStream = context.assets.open("calendar.json")
            val jsonText = jsonStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonText)

            val today = Calendar.getInstance()
            val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val todayStr = sdf.format(today.time)

            var foundData = CalendarDayData(Date = todayStr, Gujarati = "ડેટા મળ્યો નથી")

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                // શીટના નવા હેડર ENGLISH મુજબ ચેક કરવું
                if (obj.optString("ENGLISH") == todayStr) {
                    foundData = CalendarDayData(
                        Date = obj.optString("ENGLISH"),
                        Gujarati = obj.optString("ગુજરાતી (Gujarati)")
                    )
                    break
                }
            }
            foundData
        } catch (e: Exception) {
            CalendarDayData(Date = "Error", Gujarati = "Error")
        }
    }
}
