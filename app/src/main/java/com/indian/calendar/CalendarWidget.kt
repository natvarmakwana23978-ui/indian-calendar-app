fun loadCalendarData(context: Context): List<CalendarDayData> {
    val jsonString = context.assets.open("your_file.json")
        .bufferedReader()
        .use { it.readText() }
    val gson = Gson()
    val type = object : TypeToken<List<CalendarDayData>>() {}.type
    return gson.fromJson(jsonString, type)
}

fun getWidgetLines(dayData: CalendarDayData): List<String> {
    val firstLine = "${dayData.Date}, ${dayData.Day}"
    val secondLine = "${dayData.Gujarati_Month} ${dayData.Tithi}, ${dayData.Day}"
    val thirdLine = dayData.Festival_English.ifEmpty { "" }
    val fourthLine = "" // Special day logic અહીં ભરો
    return listOf(firstLine, secondLine, thirdLine, fourthLine)
}
