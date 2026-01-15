// When user selects a calendar from list
calendarListView.setOnItemClickListener { parent, view, position, id ->
    val selectedCalendar = calendarList[position] // From API
    val colIndex = position // Or use API-provided index
    PreferencesHelper.saveSelectedCalendar(this, selectedCalendar.calendarName, colIndex)
    fetchCalendarDetails(colIndex)
}

// Fetch calendar details function
private fun fetchCalendarDetails(colIndex: Int) {
    if (colIndex == -1) return
    lifecycleScope.launch {
        try {
            val calendarData = RetrofitClient.instance.getCalendarData(colIndex)
            calendarRecyclerView.adapter = CalendarAdapter(calendarData)
        } catch (e: Exception) {
            Log.e("CalendarFetch", "Error: ${e.message}")
            Toast.makeText(this@CalendarSelectionActivity, "Failed to fetch calendar details", Toast.LENGTH_SHORT).show()
        }
    }
}
