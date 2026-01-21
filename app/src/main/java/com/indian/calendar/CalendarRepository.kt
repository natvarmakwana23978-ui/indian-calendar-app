package com.indian.calendar

import org.json.JSONArray

class CalendarRepository {
    fun parseJson(jsonText: String): List<CalendarDayData> {
        val list = mutableListOf<CalendarDayData>()
        val jsonArray = JSONArray(jsonText)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            
            // અહી માત્ર CalendarDayData માં રહેલા સાચા પેરામીટર જ વાપર્યા છે [cite: 2026-01-21]
            list.add(CalendarDayData(
                Date = obj.optString("ENGLISH"),
                Gujarati = obj.optString("local_date"), // સ્ક્રિપ્ટમાંથી આવતું સાચું Key
                Alert = obj.optString("Alert")
            ))
        }
        return list
    }
}
