package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.calendar_widget)
            val prefs = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            
            val selectedLang = prefs.getString("language", "gu") ?: "gu"
            val selectedCal = prefs.getString("calendar_type", "indian") ?: "indian"
            val now = Date()

            // લાઈન ૧: ગ્રેગોરિયન તારીખ (અંકોના સુધારા સાથે)
            val df1 = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale(selectedLang))
            var line1Text = df1.format(now)
            if (selectedLang == "gu") line1Text = GlobalCalendarManager.convertToLocalNumbers(line1Text)
            views.setTextViewText(R.id.date_line_1, line1Text)

            // લાઈન ૨: ગ્લોબલ કેલેન્ડર
            val line2Text = GlobalCalendarManager.getFormattedDate(selectedCal, selectedLang)
            views.setTextViewText(R.id.date_line_2, line2Text)

            // લાઈન ૩: તહેવારો (લોજિક મુજબ)
            val showAll = prefs.getBoolean("show_all_festivals", false)
            val showSpecial = prefs.getBoolean("show_special_days", true)
            // (અહીં તહેવારોનું JSON લોજિક આવશે જે આપણે આગામી સ્ટેપમાં ફાઈનલ કરીશું)
            views.setTextViewText(R.id.date_line_3, "તહેવારોની વિગત")

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
