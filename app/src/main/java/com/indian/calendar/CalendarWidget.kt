package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
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
        val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
        val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
        val appWidgetManagerInstance = AppWidgetManager.getInstance(context)

        val selectedKey = sharedPref.getString("selected_key", "vikram_samvat") ?: "vikram_samvat"
        val targetLang = sharedPref.getString("selected_language", TranslateLanguage.ENGLISH) ?: TranslateLanguage.ENGLISH

        // આજની તારીખ મેળવો
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val currentDate = sdf.format(calendar.time)

        try {
            val inputStream = context.assets.open("json/calendar_2082.json")
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            val rootObject = JSONObject(jsonText)

            if (rootObject.has(currentDate)) {
                val dateData = rootObject.getJSONObject(currentDate)
                val calendars = dateData.getJSONObject("calendars")

                // ૧. અંગ્રેજી તારીખ સેટ કરો (હંમેશા અંગ્રેજી રાખવી)
                val greg = calendars.getJSONObject("gregorian")
                val englishDate = "${greg.getString("date")} ${greg.getString("month")}, ${greg.getString("year")} - ${greg.getString("day")}"
                views.setTextViewText(R.id.widget_english_date, englishDate)

                // ૨. તિથિ અને તહેવારનો ડેટા તૈયાર કરો
                val calObj = calendars.getJSONObject(selectedKey)
                val month = calObj.getString("month")
                val date = calObj.getString("date")
                val festivalArray = dateData.optJSONArray("festivals")
                val festName = if (festivalArray != null && festivalArray.length() > 0) festivalArray.getJSONObject(0).getString("name") else ""
                
                val rawText = "$month - $date\n$festName"

                // ૩. ટ્રાન્સલેશન પ્રોસેસ (બેટરી સેવર મોડ)
                if (targetLang != TranslateLanguage.ENGLISH) {
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(targetLang)
                        .build()
                    val translator = Translation.getClient(options)

                    // મોડેલ ચેક કરો અને ટ્રાન્સલેટ કરો
                    translator.downloadModelIfNeeded()
                        .addOnSuccessListener {
                            translator.translate(rawText)
                                .addOnSuccessListener { translated ->
                                    views.setTextViewText(R.id.widget_date_text, translated)
                                    appWidgetManagerInstance.updateAppWidget(appWidgetId, views)
                                    translator.close() // કામ પૂરું થાય એટલે તરત બંધ કરો જેથી બેટરી બચે
                                }
                        }
                        .addOnFailureListener {
                            views.setTextViewText(R.id.widget_date_text, rawText)
                            appWidgetManagerInstance.updateAppWidget(appWidgetId, views)
                        }
                } else {
                    views.setTextViewText(R.id.widget_date_text, rawText)
                    appWidgetManagerInstance.updateAppWidget(appWidgetId, views)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

