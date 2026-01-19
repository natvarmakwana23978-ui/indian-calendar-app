package com.indian.calendar

import org.json.JSONArray
// 'model' ઈમ્પોર્ટ હટાવી દીધો છે

class CalendarRepository {
    fun parseJson(jsonText: String): List<CalendarDayData> {
        val list = mutableListOf<CalendarDayData>()
        val jsonArray = JSONArray(jsonText)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            
            // તમારી નવી CalendarDayData ફાઈલમાં જે વેરીએબલ નામ છે તે જ અહીં વાપર્યા છે
            list.add(CalendarDayData(
                Date = obj.optString("ENGLISH"),
                Gujarati = obj.optString("ગુજરાતી (Gujarati)"),
                Hindi = obj.optString("हिन्दी (Hindi)")
                // જો બીજી ભાષાઓની જરૂર હોય તો અહીં ઉમેરી શકાય, 
                // પણ CalendarDayData માં તે default ખાલી જ રહેશે.
            ))
        }
        return list
    }
}
