package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget)
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        thread {
            val db = AppDatabase.getDatabase(context)
            val noteEntry = db.userNoteDao().getNoteByDate(currentDate)
            
            // અહીં 'personalNote' ને બદલે 'note' વાપર્યું છે
            val displayText = noteEntry?.note ?: "આજે કોઈ નોંધ નથી"
            
            views.setTextViewText(R.id.widget_note_text, displayText)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
