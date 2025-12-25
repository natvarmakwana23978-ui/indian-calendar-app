package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
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
        
        // આજના દિવસની તારીખ મેળવો (Format: YYYY-MM-DD)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())

        // અહીં આપણે હાલ પૂરતું ટેસ્ટિંગ માટે મેન્યુઅલ ટેક્સ્ટ સેટ કરીએ છીએ
        // જ્યારે JSON લોજિક પુરેપુરુ સેટ થશે ત્યારે આ ડાયનેમિક થઈ જશે
        views.setTextViewText(R.id.widget_date_text, "જાન્યુઆરી ૨૦૨૬ ટેસ્ટ")
        views.setTextViewText(R.id.widget_festival_text, "ટેસ્ટિંગ ચાલુ છે...")

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

