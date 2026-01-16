package com.indian.calendar

data class CalendarItem(
    val id: String = "",
    val name: String = "",       // દા.ત. "ગુજરાતી (Gujarati)"
    val colIndex: Int = 0        // શીટમાં આ ભાષા કયા નંબરના કોલમમાં છે તે
)
