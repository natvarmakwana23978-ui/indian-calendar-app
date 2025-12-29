package com.indian.calendar

import java.util.*
import java.util.concurrent.TimeUnit

object CalendarEngine {

    // ૧. પારસી રોજ ગણવાનું લોજિક (JSON માં ડેટા ન હોય તો પણ ચાલશે)
    fun getParsiDetails(date: Date, lang: String): String {
        // પારસી કેલેન્ડરનો એક રેફરન્સ પોઈન્ટ (૨૧ માર્ચ ૨૦૨૫ એ રોજ હોર્મઝદ હોય છે)
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val referenceDate = formatter.parse("2025-03-21")!!
        
        val diffInMs = date.time - referenceDate.time
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMs).toInt()
        
        // ૩૦ દિવસની લૂપ મુજબ રોજ શોધવો
        val rozIndex = ((diffInDays % 30) + 30) % 30
        val rozName = LanguageMapper.parsiRoz[lang]?.get(rozIndex) ?: LanguageMapper.parsiRoz["en"]!![rozIndex]
        
        // મહિનો (માહ) શોધવો (પારસી કેલેન્ડર મુજબ દર ૩૦ દિવસે મહિનો બદલાય)
        val mahIndex = (((diffInDays / 30) % 12) + 12) % 12 + 1
        
        return if (lang == "gu") "રોજ: $rozName, માહ: $mahIndex" else "Roz: $rozName, Mah: $mahIndex"
    }

    // ૨. અન્ય કોઈ પણ કેલેન્ડર માટે જનરલ ફોર્મેટર
    fun formatCalendarDisplay(rawJsonData: String, calendarType: String, lang: String): String {
        // જો JSON માં ડેટા "1447/06/27" જેવો હોય તો તેને સજાવવો
        if (rawJsonData.contains("/") || rawJsonData.contains("-")) {
            val parts = rawJsonData.split("/", "-")
            if (parts.size >= 3) {
                val year = parts[0]
                val month = parts[1].toIntOrNull() ?: 1
                val day = parts[2]

                return when (calendarType) {
                    "islamic" -> {
                        val mName = LanguageMapper.islamicMonths[lang]?.get(month - 1) ?: parts[1]
                        if (lang == "gu") LanguageMapper.toGujaratiDigits("$day $mName, $year") 
                        else "$day $mName, $year"
                    }
                    "saka" -> {
                        val mName = LanguageMapper.sakaMonths[lang]?.get(month - 1) ?: parts[1]
                        if (lang == "gu") LanguageMapper.toGujaratiDigits("$day $mName, $year")
                        else "$day $mName, $year"
                    }
                    else -> LanguageMapper.toGujaratiDigits(rawJsonData)
                }
            }
        }
        return if (lang == "gu") LanguageMapper.toGujaratiDigits(rawJsonData) else rawJsonData
    }
}

