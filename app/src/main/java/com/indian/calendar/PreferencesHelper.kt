package com.indian.calendar

import android.content.Context

object PreferencesHelper {
    private const val PREFS_NAME = "AppPrefs"
    private const val KEY_CALENDAR_NAME = "selectedCalendar"
    private const val KEY_COL_INDEX = "selectedColIndex"

    fun saveSelectedCalendar(context: Context, calendarName: String, colIndex: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_CALENDAR_NAME, calendarName)
            putInt(KEY_COL_INDEX, colIndex)
            apply()
        }
    }

    fun getSelectedColIndex(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_COL_INDEX, -1)
    }
}
