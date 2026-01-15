package com.indian.calendar

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateAllWidgets(context)
    }

    companion object {

        fun updateAllWidgets(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, CalendarWidget::class.java))
            ids.forEach { widgetId ->
                updateWidget(context, manager, widgetId)
            }
        }

        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.calendar_widget)

            // English date
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val today = dateFormat.format(Date())
            views.setTextViewText(R.id.tvWidgetDate, today)

            // Local calendar info – demo
            val colIndex = PreferencesHelper.getSelectedColIndex(context)
            val tithi = if (colIndex != -1) "Purnima" else ""
            views.setTextViewText(R.id.tvWidgetTithi, tithi)

            // Next reminder – fetch dynamically
            val nextReminder = getNextReminder(context)
            views.setTextViewText(R.id.tvWidgetReminder, nextReminder)

            // Open app on click
            val intent = Intent(context, CalendarSelectionActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(widgetId, views)
        }

        private fun getNextReminder(context: Context): String {
            val prefs = context.getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
            val set = prefs.getStringSet("user_reminders", emptySet()) ?: emptySet()
            if (set.isEmpty()) return "No upcoming reminders"

            // Parse reminders and sort by time (HH:mm)
            val todayReminders = set.mapNotNull {
                val parts = it.split("|")
                if (parts.size == 2) Pair(parts[0], parts[1]) else null
            }.sortedBy { it.first } // simple string sort

            // Return first upcoming
            return "Next: ${todayReminders.first().second} at ${todayReminders.first().first}"
        }
    }
}
