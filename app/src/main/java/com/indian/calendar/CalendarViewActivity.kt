// ... જૂનો કોડ (imports વગેરે) ...

if (!jsonData.isNullOrEmpty()) {
    val dataList: List<JsonObject> = Gson().fromJson(
        jsonData, object : TypeToken<List<JsonObject>>() {}.type
    )

    // ડેટાને CalendarDayData માં રૂપાંતરિત કરો
    val daysList = mutableListOf<CalendarDayData?>()

    // પગલું ૫ મુજબ: ૧ જાન્યુઆરી ૨૦૨૬ એ ગુરુવાર છે.
    // રવિવાર(0), સોમવાર(1), મંગળવાર(2), બુધવાર(3) -> આ ૪ ખાના ખાલી છોડવા પડશે
    for (i in 1..4) {
        daysList.add(null)
    }

    // શીટના દરેક રો (Row) ને લિસ્ટમાં ઉમેરો
    dataList.forEach { json ->
        val dateStr = json.get("ENGLISH")?.asString ?: ""
        daysList.add(CalendarDayData(dateStr, json))
    }

    tvHeader.text = "કેલેન્ડર ૨૦૨૬"
    
    // હવે daysList (જે List<CalendarDayData?> છે) તે એડેપ્ટરને મોકલો
    recyclerView.adapter = CalendarAdapter(daysList, selectedLang)
}
