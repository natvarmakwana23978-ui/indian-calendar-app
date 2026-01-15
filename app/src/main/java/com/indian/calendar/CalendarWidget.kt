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

            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            views.setTextViewText(R.id.tvWidgetDate, dateFormat.format(Date()))

            val colIndex = PreferencesHelper.getSelectedColIndex(context)
            val tithi = if (colIndex != -1) "Purnima" else ""
            views.setTextViewText(R.id.tvWidgetTithi, tithi)

            // Get next future reminder
            views.setTextViewText(R.id.tvWidgetReminder, getNextFutureReminder(context))

            val intent = Intent(context, CalendarSelectionActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(widgetId, views)
        }

        private fun getNextFutureReminder(context: Context): String {
            val prefs = context.getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
            val set = prefs.getStringSet("user_reminders", emptySet()) ?: emptySet()
            if (set.isEmpty()) return "No upcoming reminders"

            val now = Calendar.getInstance()
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            val futureReminders = set.mapNotNull {
                val parts = it.split("|")
                if (parts.size == 2) Pair(parts[0], parts[1]) else null
            }.filter {
                try {
                    val calTime = Calendar.getInstance()
                    val date = timeFormat.parse(it.first) ?: return@filter false
                    calTime.time = date
                    val nowMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
                    val reminderMinutes = calTime.get(Calendar.HOUR_OF_DAY) * 60 + calTime.get(Calendar.MINUTE)
                    reminderMinutes >= nowMinutes
                } catch (e: Exception) { false }
            }.sortedBy { it.first }

            return if (futureReminders.isEmpty()) "No upcoming reminders"
            else "Next: ${futureReminders.first().second} at ${futureReminders.first().first}"
        }
    }
}
