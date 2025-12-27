package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import org.json.JSONObject
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: IntArray) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget)
        val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)

        // ૧. પસંદ કરેલ કેલેન્ડર અને ભાષા મેળવો
        val selectedKey = sharedPref.getString("selected_key", "vikram_samvat") ?: "vikram_samvat"
        val targetLang = sharedPref.getString("selected_language", TranslateLanguage.ENGLISH) ?: TranslateLanguage.ENGLISH

        // ૨. આજની તારીખ મુજબ જેસોન ડેટા વાંચો (અંગ્રેજીમાં)
        val todayData = getTodayCalendarData(context, selectedKey)
        val rawText = "Festival: ${todayData.getString("festival")}\nTithi: ${todayData.getString("tithi")}"

        // ૩. જો ભાષા અંગ્રેજી ન હોય, તો ટ્રાન્સલેટ કરો
        if (targetLang != TranslateLanguage.ENGLISH) {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLang)
                .build()
            
            val translator = Translation.getClient(options)
            
            // ઓફલાઇન ટ્રાન્સલેશન પ્રોસેસ
            translator.translate(rawText)
                .addOnSuccessListener { translatedText ->
                    views.setTextViewText(R.id.widget_text, translatedText)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .addOnFailureListener {
                    // જો ટ્રાન્સલેશન ફેઈલ જાય તો અંગ્રેજી લખાણ બતાવો
                    views.setTextViewText(R.id.widget_text, rawText)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
        } else {
            // સીધું અંગ્રેજી લખાણ
            views.setTextViewText(R.id.widget_text, rawText)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getTodayCalendarData(context: Context, key: String): JSONObject {
        // આ ફંક્શન તમારી assets માં રહેલી .json ફાઈલમાંથી આજની તારીખનો ડેટા લાવશે
        // તમારી જૂની રીત મુજબ અહીં JSON parsing લોજિક ચાલુ રહેશે
        val jsonString = context.assets.open("calendar_data.json").bufferedReader().use { it.readText() }
        val root = JSONObject(jsonString)
        val calendarArray = root.getJSONArray(key)
        
        // ઉદાહરણ તરીકે અત્યારે પહેલી એન્ટ્રી લઈએ છીએ, તમારે તારીખ મુજબ ફિલ્ટર કરવાનું રહેશે
        return calendarArray.getJSONObject(0) 
    }
}

