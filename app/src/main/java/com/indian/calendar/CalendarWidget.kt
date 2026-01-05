package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        for (id in ids) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            val today = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
            views.setTextViewText(R.id.widgetDate, SimpleDateFormat("d MMM, EEE", Locale.getDefault()).format(Date()))

            CoroutineScope(Dispatchers.IO).launch {
                val note = AppDatabase.getDatabase(context).userNoteDao().getNoteByDate(today)
                views.setTextViewText(R.id.widgetNote, note?.personalNote ?: "આજનો દિવસ શુભ રહે!")
                manager.updateAppWidget(id, views)
            }
        }
    }
}

