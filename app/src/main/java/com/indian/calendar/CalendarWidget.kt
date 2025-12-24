package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)

        // ૧. ગ્રેગોરિયન તારીખ સેટ કરવી (જમણું બોક્સ)
        val fullDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
        val dateOnly = SimpleDateFormat("dd", Locale.getDefault()).format(Date())
        val monthEng = SimpleDateFormat("MMMM", Locale.ENGLISH).format(Date())
        val yearEng = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())

        views.setTextViewText(R.id.txt_date, dateOnly)
        views.setTextViewText(R.id.txt_month_eng, monthEng)
        views.setTextViewText(R.id.txt_year, "A.D. $yearEng")

        // ૨. JSON માંથી ગુજરાતી ડેટા મેળવવો (ડાબું બોક્સ)
        val todayData = getTodayJsonData(context, fullDate)

        if (todayData != null) {
            val tithiWithNo = todayData.getString("Tithi") // "વદ-૧૩"
            val monthGuj = todayData.getString("Month")   // "માગશર"
            val dayGuj = todayData.getString("Day")       // "બુધવાર"

            // તિથિમાંથી ફક્ત નંબર અલગ પાડવો હોય તો (વદ-૧૩ માંથી ૧૩)
            val tithiNo = tithiWithNo.split("-").lastOrNull() ?: tithiWithNo

            views.setTextViewText(R.id.txt_tithi, tithiNo)
            views.setTextViewText(R.id.txt_guj_month, "$monthGuj $tithiWithNo")
            views.setTextViewText(R.id.txt_guj_day, dayGuj)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getTodayJsonData(context: Context, todayDate: String): JSONObject? {
        return try {
            val inputStream = context.assets.open("calendar_data.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.getString("Date") == todayDate) {
                    return obj
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

