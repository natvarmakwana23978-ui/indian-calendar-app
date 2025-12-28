import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        
        // ૧. આજની તારીખ મેળવો
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val today = sdf.format(Date())
        val displayDate = SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.getDefault()).format(Date())

        // ૨. JSON માંથી ડેટા વાંચો
        val jsonString = context.assets.open("json/calendar_2082_global.json").bufferedReader().use { it.readText() }
        val rootObj = JSONObject(jsonString)

        if (rootObj.has(today)) {
            val dayData = rootObj.getJSONObject(today)
            val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            val selectedCalKey = prefs.getString("selected_calendar", "islamic") ?: "islamic"
            
            val globalCals = dayData.getJSONObject("global_calendars")
            val gujaratiInfo = dayData.getJSONObject("gujarati_info")
            
            // લાઇન ૧: ઇંગ્લિશ તારીખ
            views.setTextViewText(R.id.txtLine1, displayDate)

            // લાઇન ૨: યુઝરની પસંદગીનું કેલેન્ડર
            val calValue = globalCals.optString(selectedCalKey, "")
            views.setTextViewText(R.id.txtLine2, calValue)

            // લાઇન ૩: તહેવાર + વિશેષ દિવસ (જો ખાલી હોય તો કઈ ન બતાવે)
            val festival = gujaratiInfo.optString("festival", "")
            val specialDay = dayData.optString("special_day", "")
            
            var line3Text = festival
            if (specialDay.isNotEmpty()) {
                line3Text += if (line3Text.isNotEmpty()) " | $specialDay" else specialDay
            }
            
            views.setTextViewText(R.id.txtLine3, if (line3Text.isEmpty()) "સામાન્ય દિવસ" else line3Text)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

