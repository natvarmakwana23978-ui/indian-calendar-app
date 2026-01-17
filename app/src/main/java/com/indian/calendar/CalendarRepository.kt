package com.indian.calendar

import com.indian.calendar.model.CalendarDayData
import org.json.JSONArray

class CalendarRepository {
    fun parseJson(jsonText: String): List<CalendarDayData> {
        val list = mutableListOf<CalendarDayData>()
        val jsonArray = JSONArray(jsonText)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(CalendarDayData(
                Date = obj.optString("ENGLISH"),
                Gujarati = obj.optString("ગુજરાતી (Gujarati)")
            ))
        }
        return list
    }
}
