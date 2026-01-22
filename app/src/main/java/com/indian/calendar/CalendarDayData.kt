package indian.calendar

import com.google.gson.JsonObject

data class CalendarDayData(
    val englishDate: String,  // કોલમ A (ENGLISH)
    val localDate: String?,   // કોલમ B (ગુજરાતી (Gujarati))
    val allData: JsonObject   // આખી રો નો ડેટા (બધી ૨૭ કોલમ્સ માટે)
)
